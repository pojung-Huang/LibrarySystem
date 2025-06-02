from sqlalchemy import create_engine
from dotenv import load_dotenv
import mysql.connector
import os
import json
from datetime import datetime

# 載入環境變數
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

# if conn.is_connected():
#     cursor.execute("SELECT * FROM library_books")
#     books_data = cursor.fetchall()
# else:
#     print("連接失敗")

def save_to_mysql(books_data):
    try:
        print("================================================"*10)
        print("開始存入資料庫")
        print(f"存入資料庫時間: {datetime.now()}")

        sql = """
                INSERT INTO library_books (isbn, title, author, publisher, publish_date)
                VALUES (%s, %s, %s, %s, %s)
                """
        values = list()
        
        for book in books_data:
            if book['isbn'] == '' or book['title'] == '' or book['author'] == '' or book['publisher'] == '' or book['publish_date'] == '':
                pass
            else:
                values.append((book['isbn'], book['title'], book['author'], book['publisher'], book['publish_date']))
        # values = [(d['isbn'], d['title'], d['author'], d['publisher'], d['publish_date']) for d in books_data]
        # print(values)


        cursor.executemany(sql, values)
        conn.commit()

        print(f"存入資料庫時間: {datetime.now()}")
        print("存入資料庫完成")
        print("================================================"*10)

    except Exception as e:
        print(f"❌ 資料庫儲存錯誤: {str(e)}")
        raise e


# def save_to_mysql(books_data):
#     # 從環境變數獲取資料庫配置
#     DB_USER = os.getenv('root')
#     DB_PASSWORD = os.getenv('uTXa3ZqJ1DMHR9WypFboLgYcKsvfNe58')
#     DB_HOST = os.getenv('localhost')
#     DB_PORT = os.getenv('3308')
#     DB_NAME = os.getenv('library')
    # DB_URL = os.getenv('mysql+pymysql://root:uTXa3ZqJ1DMHR9WypFboLgYcKsvfNe58@localhost:3308/library')


    # 檢查必要的環境變數是否存在
    # if not all([DB_USER, DB_PASSWORD, DB_HOST, DB_PORT, DB_NAME]):
    #     raise ValueError("缺少必要的資料庫配置環境變數")
    # connection_string = 'mysql+pymysql://root:uTXa3ZqJ1DMHR9WypFboLgYcKsvfNe58@localhost:3308/library'

    # 建立資料庫連接字串
    # connection_string = f"mysql+pymysql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}"
    # print(f"連接字串: {connection_string}")
    
    # try:
        # engine = create_engine(connection_string)
        # engine = create_engine(DB_URL)



        # with engine.connect() as connection:
        #     for book in books_data:
        #         connection.execute(sql, {"book_data": json.dumps(book, ensure_ascii=False)})
        #     connection.commit()

    # except Exception as e:
    #     print(f"❌ 資料庫儲存錯誤: {str(e)}")
    #     raise e
