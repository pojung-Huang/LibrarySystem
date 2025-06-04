# from sqlalchemy import create_engine
from dotenv import load_dotenv
import mysql.connector
import os
# import json
from datetime import datetime


env_path = os.path.join(os.path.dirname(os.path.dirname(__file__)), '.env')
load_dotenv(env_path, override=True)

conn = mysql.connector.connect(
    host=os.getenv('DB_HOST'),
    user=os.getenv('DB_USER'),
    password=os.getenv('DB_PASSWORD'),
    port=os.getenv('DB_PORT'),
    database=os.getenv('DB_NAME'),
    charset='utf8'
)
cursor = conn.cursor()

def save_to_mysql(books_data):
    try:
        print("開始存入資料庫")
        print(f"存入資料庫時間: {datetime.now()}")

        sql = """
                INSERT INTO library_books (isbn, title, author, publisher, publishdate)
                VALUES (%s, %s, %s, %s, %s)
                """

        values = list()
        for book in books_data:
            if book['isbn'] == '' or book['title'] == '' or book['author'] == '' or book['publisher'] == '' or book['publishdate'] == '':
                pass
            else:
                values.append((book['isbn'], book['title'], book['author'], book['publisher'], book['publishdate']))

        cursor.executemany(sql, values)
        conn.commit()

        print(f"存入資料庫時間: {datetime.now()}")
        print(f"存入資料庫成功")

    except Exception as e:
        print(f"❌ 資料庫儲存錯誤: {str(e)}")
        raise e
