package tw.ispan.librarysystem.repository.borrow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.ispan.librarysystem.entity.borrow.Borrow;
import java.util.List;
import java.math.BigInteger;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, BigInteger> {
    List<Borrow> findByMemberId(Integer memberId);
    List<Borrow> findByBookId(Integer bookId);
    List<Borrow> findByStatus(String status);
    
    @Query("SELECT b FROM Borrow b WHERE b.memberId = :memberId ORDER BY b.borrowDate DESC")
    List<Borrow> findMemberBorrowHistory(@Param("memberId") Integer memberId);
    
    @Query("SELECT b FROM Borrow b WHERE b.memberId = :memberId AND b.status = :status ORDER BY b.borrowDate DESC")
    List<Borrow> findMemberBorrowsByStatus(@Param("memberId") Integer memberId, @Param("status") String status);
} 