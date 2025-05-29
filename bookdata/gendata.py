import os
import pandas as pd

RAW_DIR = 'data/raw'
OUTPUT_DIR = 'data/processed'

# 清洗設定
COLUMN_MAPPING = {
    'ISBN (020$a$c)': 'ISBN',
    '書名 (245$a$b)': '書名',
    '編著者 (245$c)': '作者',
    '出版項 (260)': '出版社',
    '出版年 (008/07-10)': '出版日期'
}

ENCODING = 'utf-8-sig'  # 如果中文亂碼可試 'big5' 或 'utf-8-sig'

def clean_dataframe(df: pd.DataFrame) -> pd.DataFrame:
    # 重新命名欄位
    df = df.rename(columns=COLUMN_MAPPING)
    
    # 只保留需要的欄位
    required_columns = list(COLUMN_MAPPING.values())
    df = df[required_columns]

    # 移除完全空值的行
    df.dropna(how='all', inplace=True)

    # 去除重複書籍（根據書名 + 作者）
    df.drop_duplicates(subset=['書名', '作者'], inplace=True)

    # 替換空字串為 NaN，並移除重要欄位空值
    df.replace('', pd.NA, inplace=True)
    df.dropna(subset=['書名', 'ISBN'], inplace=True)

    # 去除前後空白字元
    for col in df.select_dtypes(include='object'):
        df[col] = df[col].str.strip()

    # 清理 ISBN（移除非數字字元）
    df['ISBN'] = df['ISBN'].str.replace(r'[^\d]', '', regex=True)

    # 清理出版日期（只保留年份）
    df['出版日期'] = df['出版日期'].str.extract(r'(\d{4})')

    return df.reset_index(drop=True)

def main():
    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)

    for filename in os.listdir(RAW_DIR):
        if filename.endswith('.csv'):
            raw_path = os.path.join(RAW_DIR, filename)
            print(f'📥 讀取檔案: {raw_path}')

            # 讀取 CSV 檔案，跳過第一行（欄位名稱）
            df = pd.read_csv(raw_path, encoding=ENCODING)

            df_clean = clean_dataframe(df)

            out_path = os.path.join(OUTPUT_DIR, f'cleaned_{filename}')
            df_clean.to_csv(out_path, index=False, encoding='utf-8-sig')

            print(f'✅ 清洗完成，已輸出到: {out_path}\n')

# class BookDataProcessor:
#     def __init__(self):
#         self.db_handler = DatabaseHandler()
        
#     def process_and_save(self):
#         # 1. 讀取原始資料
#         raw_data = self.read_raw_data()
        
#         # 2. 資料清理和轉換
#         processed_data = self.preprocess_data(raw_data)
        
#         # 3. 存入資料庫
#         self.db_handler.save_books(processed_data)
        
#     def read_raw_data(self):
#         # 從檔案讀取原始資料
#         pass
        
#     def preprocess_data(self, raw_data):
#         # 資料清理和轉換
#         pass

if __name__ == '__main__':
    main()
