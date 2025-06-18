package tw.ispan.librarysystem.entity.books;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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

    @OneToOne
    @MapsId
    @JoinColumn(name = "book_id")
    private BookEntity book;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    
}
