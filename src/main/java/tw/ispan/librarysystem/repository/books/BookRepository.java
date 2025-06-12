package tw.ispan.librarysystem.repository.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.ispan.librarysystem.entity.books.BookEntity;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    // 基本搜尋
    List<BookEntity> findByTitleContainingIgnoreCase(String title);
    
    // 多條件搜尋
    @Query("SELECT b FROM BookEntity b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:publisher IS NULL OR LOWER(b.publisher) LIKE LOWER(CONCAT('%', :publisher, '%'))) AND " +
           "(:isbn IS NULL OR b.isbn LIKE CONCAT('%', :isbn, '%')) AND " +
           "(:language IS NULL OR b.language = :language) AND " +
           "(:classification IS NULL OR b.classification = :classification)")
    Page<BookEntity> searchBooks(
        @Param("title") String title,
        @Param("author") String author,
        @Param("publisher") String publisher,
        @Param("isbn") String isbn,
        @Param("language") String language,
        @Param("classification") String classification,
        Pageable pageable
    );
}