package tw.ispan.librarysystem.repository.borrow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.ispan.librarysystem.entity.borrow.Borrow;
import tw.ispan.librarysystem.entity.borrow.Borrow.BorrowStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {
    
    // 基本查詢
    List<Borrow> findByUserId(Integer userId);
    List<Borrow> findByBookId(Integer bookId);
    List<Borrow> findByStatus(BorrowStatus status);
    
    // 分頁查詢
    Page<Borrow> findByUserId(Integer userId, Pageable pageable);
    Page<Borrow> findByBookId(Integer bookId, Pageable pageable);
    Page<Borrow> findByStatus(BorrowStatus status, Pageable pageable);
    
    // 複合查詢
    @Query("SELECT b FROM Borrow b WHERE b.userId = :userId AND b.status = :status")
    List<Borrow> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") BorrowStatus status);
    
    @Query("SELECT b FROM Borrow b WHERE b.bookId = :bookId AND b.status = :status")
    List<Borrow> findByBookIdAndStatus(@Param("bookId") Integer bookId, @Param("status") BorrowStatus status);
    
    // 借閱歷史查詢
    @Query("SELECT b FROM Borrow b WHERE b.userId = :userId ORDER BY b.borrowDate DESC")
    List<Borrow> findMemberBorrowHistory(@Param("userId") Integer userId);
    
    // 逾期查詢
    @Query("SELECT b FROM Borrow b WHERE b.dueDate < :now AND b.status IN ('BORROWED', 'RENEWED')")
    List<Borrow> findOverdueBorrows(@Param("now") LocalDateTime now);
    
    // 統計查詢
    @Query("SELECT COUNT(b) FROM Borrow b WHERE b.userId = :userId AND b.status = :status")
    long countByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") BorrowStatus status);
    
    @Query("SELECT COUNT(b) FROM Borrow b WHERE b.bookId = :bookId AND b.status = :status")
    long countByBookIdAndStatus(@Param("bookId") Integer bookId, @Param("status") BorrowStatus status);
    
    // 檢查是否有未歸還的借閱
    @Query("SELECT COUNT(b) > 0 FROM Borrow b WHERE b.userId = :userId AND b.bookId = :bookId AND b.status IN ('BORROWED', 'RENEWED')")
    boolean existsActiveBookBorrow(@Param("userId") Integer userId, @Param("bookId") Integer bookId);
} 