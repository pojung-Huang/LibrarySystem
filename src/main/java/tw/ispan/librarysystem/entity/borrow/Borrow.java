package tw.ispan.librarysystem.entity.borrow;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import tw.ispan.librarysystem.entity.member.Member;
import tw.ispan.librarysystem.entity.books.BookEntity;
import java.math.BigInteger;
import java.util.Date;

/**
 * 借閱記錄實體類別
 */
@Entity
@Table(name = "borrow_records")
public class Borrow {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    private Integer borrowId;

    @Column(name = "user_id")
    private Integer memberId;

    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "borrow_date")
    private Date borrowDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "renew_count")
    private Integer renewCount;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    // 關聯
    @ManyToOne
    @JoinColumn(name = "user_id", insertable=false, updatable=false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable=false, updatable=false)
    private BookEntity book;

    public Integer getBorrowId() {
        return borrowId;
    }
    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Integer getMemberId() {
        return memberId;
    }
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getBookId() {
        return bookId;
    }
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getRenewCount() {
        return renewCount;
    }
    public void setRenewCount(Integer renewCount) {
        this.renewCount = renewCount;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() { 
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;
    }

    public BookEntity getBook() {
        return book;
    }
    public void setBook(BookEntity book) {
        this.book = book;
    }
}
