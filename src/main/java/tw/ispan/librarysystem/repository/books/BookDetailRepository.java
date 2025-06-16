

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

    @Query("SELECT b FROM BookEntity b LEFT JOIN b.bookDetail bd WHERE bd IS NULL OR bd.imgUrl IS NULL")
    List<BookEntity> findBooksWithoutCover();

    Optional<BookDetailEntity> findByBook(BookEntity book);

    
}
