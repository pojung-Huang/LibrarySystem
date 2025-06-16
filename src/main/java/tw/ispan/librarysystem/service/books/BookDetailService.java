

package tw.ispan.librarysystem.service.books;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.ispan.librarysystem.entity.books.BookDetailEntity;
import tw.ispan.librarysystem.entity.books.BookEntity;
import tw.ispan.librarysystem.repository.books.BookDetailRepository;
import tw.ispan.librarysystem.repository.books.BookRepository;

@Service
public class BookDetailService {

    @Autowired
    private BookDetailRepository bookDetailRepository;

    @Autowired
    private BookRepository bookRepository;

    public void updateMissingCoversAndSummaries() {
        List<BookEntity> booksWithoutCover = bookDetailRepository.findBooksWithoutCover();
        for (BookEntity book : booksWithoutCover) {
            String isbn = book.getIsbn();
            if (isbn == null || isbn.isBlank()) continue;
            try {
                String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    InputStream inputStream = conn.getInputStream();
                    String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(json);
                    JsonNode items = root.get("items");
                    if (items != null && items.isArray() && items.size() > 0) {
                        JsonNode volumeInfo = items.get(0).get("volumeInfo");
                        String cover = volumeInfo.path("imageLinks").path("thumbnail").asText(null);
                        String desc = volumeInfo.path("description").asText(null);

                        BookDetailEntity detail = new BookDetailEntity();
                        detail.setBook(book);
                        detail.setImgUrl(cover);
                        detail.setSummary(desc);
                        bookDetailRepository.save(detail);
                    }
                }
            } catch (Exception e) {
                // Log exception
                System.err.println("無法取得 ISBN 封面: " + isbn);
            }
        }
    }
}