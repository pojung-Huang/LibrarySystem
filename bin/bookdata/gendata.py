import os
import re
import pandas as pd
import json
from utils.db_utils import save_to_mysql
from utils.es_utils import save_to_es

RAW_DIR = 'data/raw'
OUTPUT_DIR = 'data/processed'

# 欄位名稱對應
COLUMN_MAPPING = {
    'ISBN (020$a$c)': 'ISBN',
    '書名 (245$a$b)': '書名',
    '編著者 (245$c)': '作者',
    '出版項 (260)': '出版社',
    '出版年 (008/07-10)': '出版日期',
    # '館藏地 (952$a)': '館藏地'
}

ENCODING = 'utf-8-sig'  # 如出現亂碼可改為 'big5'

def clean_dataframe(df: pd.DataFrame) -> pd.DataFrame:
    # 欄位重新命名與選取
    df = df.rename(columns=COLUMN_MAPPING)
    required_columns = list(COLUMN_MAPPING.values())
    df = df[required_columns]

    # 移除全空行與重複行
    df.dropna(how='all', inplace=True)
    df.drop_duplicates(subset=['書名', '作者'], inplace=True)

    # 替換空字串為 NaN，去空白
    df.replace('', pd.NA, inplace=True)
    for col in df.select_dtypes(include='object'):
        df[col] = df[col].astype(str).str.strip()
        df[col].replace('nan', pd.NA, inplace=True)

    # ✳️ 過濾非法 UTF-8 編碼字元
    def remove_invalid_utf8(s):
        if pd.isna(s):
            return s
        try:
            s.encode('utf-8')
            return s
        except UnicodeEncodeError:
            return pd.NA

    # ✳️ 過濾非 BMP 區 Unicode（emoji、𡠄 等）
    def remove_non_bmp(text):
        if pd.isna(text):
            return text
        return ''.join(c for c in text if ord(c) <= 0xFFFF)

    # 套用雙重過濾至所有文字欄位
    for col in df.select_dtypes(include='object'):
        df[col] = df[col].apply(remove_invalid_utf8).apply(remove_non_bmp)

    # 清理 ISBN（移除非數字）
    df['ISBN'] = df['ISBN'].str.replace(r'[^\d]', '', regex=True)

    # 抽出出版年（只取 4 碼年份）
    df['出版日期'] = df['出版日期'].str.extract(r'(\d{4})')

    # ✳️ 篩選有效年份（1000～2099）
    df = df[df['出版日期'].astype(float).between(1000, 2099, inclusive='both')]

    # ✳️ 限制欄位長度避免超過 VARCHAR(255)
    for col in df.columns:
        if df[col].dtype == 'object':
            df[col] = df[col].str.slice(0, 255)

    # 最終確認關鍵欄位不為空
    df.dropna(subset=required_columns, inplace=True)

    return df.reset_index(drop=True)

def save_to_json(df: pd.DataFrame, output_path: str):
    records = df.to_dict(orient='records')
    structured_data = []
    for record in records:
        structured_record = {
            'isbn': record['ISBN'],
            'title': record['書名'],
            'author': record['作者'],
            'publisher': record['出版社'],
            'publishdate': record['出版日期']
        }
        structured_data.append(structured_record)

    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(structured_data, f, ensure_ascii=False, indent=2)

def main():
    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)

    all_books = list()
    for filename in os.listdir(RAW_DIR):
        if filename.endswith('.csv'):
            raw_path = os.path.join(RAW_DIR, filename)
            print(f'📥 讀取檔案: {raw_path}')

            df = pd.read_csv(raw_path, encoding=ENCODING)
            df_clean = clean_dataframe(df)

            print("\n🧹 清理後資料前 5 筆：")
            print(df_clean.head())

            for record in df_clean.to_dict(orient='records'):
                all_books.append({
                    'isbn': record['ISBN'],
                    'title': record['書名'],
                    'author': record['作者'],
                    'publisher': record['出版社'],
                    'publishdate': record['出版日期']
                })

    output_path = os.path.join(OUTPUT_DIR, 'all_books.json')
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(all_books, f, ensure_ascii=False, indent=2)
    print(f'✅ 所有資料已合併輸出到: {output_path}')

    save_to_mysql(all_books)
    print('✅ 資料已存入 MySQL')

    # save_to_es(all_books)
    # print('✅ 資料已存入 Elasticsearch\n')

if __name__ == '__main__':
    main()
