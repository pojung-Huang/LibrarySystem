const fs = require('fs');
const mysql = require('mysql2/promise');

// 讀取 JSON 檔案
const data = JSON.parse(fs.readFileSync('zipcodes.json', 'utf-8'));

async function importData() {
  const conn = await mysql.createConnection({
    host: 'localhost',
    port: 3308,                      // ✅ 若你的 MySQL 是 Docker 3308 → 3306 對應
    user: 'root',
    password: 'uTXa3ZqJ1DMHR9WypFboLgYcKsvfNe58',
    database: 'library'
  });

  for (const item of data) {
      const county = item.COUNTYNAME;
      const town = item.TOWNNAME;
      const zip3 = item.ZIPCODE;

      // 防呆檢查
      if (!county || !town || !zip3) {
        console.warn('⚠️ 缺資料，略過：', item);
        continue;
      }

      await conn.execute(
        'INSERT INTO tw_zipcodes (county, town, zip3) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE zip3 = VALUES(zip3)',
        [county, town, zip3]
      );
    }

    console.log('✅ 匯入完成');
    await conn.end();
  }

  importData();
