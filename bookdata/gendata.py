import os
import pandas as pd

RAW_DIR = 'data/raw'
OUTPUT_DIR = 'data/processed'

# 清洗設定
REQUIRED_COLUMNS = ['書名', '作者', 'ISBN', '出版日期', '出版社']
ENCODING = 'utf-8-sig'  # 如果中文亂碼可試 'big5' 或 'utf-8-sig'

def clean_dataframe(df: pd.DataFrame) -> pd.DataFrame:
    # 去除空白欄位名稱
    df.columns = [col.strip() for col in df.columns]

    # 移除不需要欄位（如有）
    df = df[[col for col in df.columns if col in REQUIRED_COLUMNS]]

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

    return df.reset_index(drop=True)

def main():
    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)

    for filename in os.listdir(RAW_DIR):
        if filename.endswith('.csv'):
            raw_path = os.path.join(RAW_DIR, filename)
            print(f'📥 讀取檔案: {raw_path}')

            df = pd.read_csv(raw_path, encoding=ENCODING)

            df_clean = clean_dataframe(df)

            out_path = os.path.join(OUTPUT_DIR, f'cleaned_{filename}')
            df_clean.to_csv(out_path, index=False, encoding='utf-8-sig')

            print(f'✅ 清洗完成，已輸出到: {out_path}\n')

if __name__ == '__main__':
    main()
