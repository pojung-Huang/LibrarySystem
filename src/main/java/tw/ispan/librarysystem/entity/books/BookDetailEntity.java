package tw.ispan.librarysystem.entity.books;

import jakarta.persistence.*;

@Entity
@Table(name = "bookdetail")
public class BookDetailEntity {

    @Id
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "img_url")
    private String imgUrl;

     /**
     * owning side：告訴 JPA 這個欄位要 join books.book_id，
     * 並且用 @MapsId 把 PK 的值和 book.getId() 綁在一起
     */
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookEntity book;
    // Getters
    public Integer getBookId() {
        return bookId;
    }

    public String getSummary() {
        return summary;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public BookEntity getBook() {
        return book;
    }

    // Setters
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }
}
