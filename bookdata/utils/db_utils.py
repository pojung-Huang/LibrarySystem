from sqlalchemy import create_engine

def save_to_db(df):
    engine = create_engine("mysql+pymysql://root:password@localhost:3306/bookdb")
    df.to_sql("libraries", con=engine, if_exists="replace", index=False)
    print("✅ 資料已儲存至資料庫")
