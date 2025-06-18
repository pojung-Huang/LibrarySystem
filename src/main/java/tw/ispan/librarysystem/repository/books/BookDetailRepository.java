package tw.ispan.librarysystem.repository.books;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tw.ispan.librarysystem.entity.books.BookDetailEntity;
import tw.ispan.librarysystem.entity.books.BookEntity;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetailEntity, Integer> {

    // 查詢尚未有封面記錄的書籍
    @Query("SELECT b FROM BookEntity b WHERE b.bookId NOT IN (SELECT bd.book.bookId FROM BookDetailEntity bd)")
    List<BookEntity> findBooksWithoutCover();

    // 檢查某本書的詳細資料是否已存在
    boolean existsByBookId(Integer bookId);

    Optional<BookDetailEntity> findByBook(BookEntity book);

    
}
