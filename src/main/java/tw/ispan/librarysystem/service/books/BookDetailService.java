package tw.ispan.librarysystem.service.books;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.ispan.librarysystem.entity.books.BookDetailEntity;
import tw.ispan.librarysystem.entity.books.BookEntity;
import tw.ispan.librarysystem.repository.books.BookDetailRepository;
import tw.ispan.librarysystem.repository.books.BookRepository;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.*;

@Service
public class BookDetailService {

    @Autowired
    private BookDetailRepository bookDetailRepository;

    @Autowired
    private BookRepository bookRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${google.books.api.key}")
    private String apiKey;

    @Transactional
public void updateMissingCoversAndSummaries() {
    List<BookEntity> books = bookRepository.findAll();
    int total = books.size();
    int current = 0;
    int success = 0;
    int failed = 0;

    for (BookEntity book : books) {
        current++;
        Integer bookId = book.getBookId();
        String isbn = book.getIsbn();

        try {
            // 每 20 筆清除快取
            if (current % 20 == 0) {
                bookDetailRepository.flush();
                entityManager.clear();
            }

            // 如果已存在 bookdetail 就跳過
            if (bookDetailRepository.existsByBookId(bookId)) {
                System.out.printf("⏩ 已存在 BookDetail，跳過 bookId=%d\n", bookId);
                continue;
            }

            BookEntity freshBook = bookRepository.findById(bookId).orElse(null);
            if (freshBook == null) {
                System.err.println("❌ 找不到書 bookId=" + bookId);
                failed++;
                continue;
            }

            BookDetailEntity detail = new BookDetailEntity();
            detail.setBook(freshBook);

            if (isbn != null && !isbn.isBlank()) {
                String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

                int retry = 0;
                boolean successApi = false;

                while (retry < 3 && !successApi) {
                    try {
                        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(5000);

                        int code = conn.getResponseCode();

                        if (code == 200) {
                            InputStream is = conn.getInputStream();
                            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                            JsonNode root = new ObjectMapper().readTree(json);
                            JsonNode items = root.get("items");

                            if (items != null && items.isArray() && items.size() > 0) {
                                JsonNode volumeInfo = items.get(0).get("volumeInfo");
                                String cover = volumeInfo.path("imageLinks").path("thumbnail").asText(null);
                                String desc = volumeInfo.path("description").asText(null);
                                detail.setImgUrl(cover);
                                detail.setSummary(desc);
                            }

                            successApi = true;
                        } else if (code == 429) {
                            retry++;
                            System.out.println("⚠️ API 限制，等待 1 秒重試...");
                            Thread.sleep(1000);
                        } else {
                            System.err.println("❌ API 回應錯誤：" + code);
                            break;
                        }

                    } catch (Exception e) {
                        retry++;
                        System.out.println("⚠️ 發生例外，等待 5 秒重試...");
                        Thread.sleep(5000);
                    }
                }

                if (!successApi) {
                    System.err.println("❌ API 三次失敗，略過 ISBN: " + isbn);
                    failed++;
                    continue;
                }
            }

            bookDetailRepository.save(detail);
            success++;

        } catch (Exception e) {
            System.err.printf("❌ [%d/%d] 失敗 bookId=%d ISBN=%s 原因：%s\n", current, total, bookId, isbn, e.getMessage());
            failed++;
        }

        System.out.printf("✅ 進度: %d/%d (%.1f%%) - 成功: %d, 失敗: %d\n",
                current, total, (current * 100.0 / total), success, failed);
    }

    bookDetailRepository.flush();
    System.out.printf("🎉 結束，共處理 %d 筆，成功 %d 筆，失敗 %d 筆\n", total, success, failed);
    }
}
