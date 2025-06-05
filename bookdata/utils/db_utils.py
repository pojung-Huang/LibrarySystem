from dotenv import load_dotenv
import mysql.connector
import os
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
        print("清空資料表")
        # 先清空資料表
        cursor.execute("TRUNCATE TABLE LibraryBooks")
        conn.commit()
        print("資料表清空完成")

        print("開始存入資料庫")
        print(f"存入資料庫時間: {datetime.now()}")

        sql = """
                INSERT INTO LibraryBooks (isbn, title, author, publisher, publishdate)
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


# if __name__ == '__main__':
#     # 測試 將 all_books 存入資料庫 
#     import json
#     with open('data/processed/all_books.json', 'r', encoding='utf-8') as f:
#         all_books = json.load(f)

#     print(f"books_data 長度: {len(all_books)}")
#     save_to_mysql(all_books)
#     cursor.execute("SELECT max(id) FROM LibraryBooks")
#     result = cursor.fetchone()
#     print(f"資料表總筆數: {result[0]}")


# 建立LibraryBooks table 的 sql 語法
"""
CREATE TABLE `LibraryBooks` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `title` text COLLATE utf8mb4_unicode_ci,
    `author` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `publisher` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `publishdate` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_isbn` (`isbn`),
    KEY `idx_author` (`author`),
    KEY `idx_title` (`title`(255))
    ) ENGINE=InnoDB AUTO_INCREMENT=1534509 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
"""
