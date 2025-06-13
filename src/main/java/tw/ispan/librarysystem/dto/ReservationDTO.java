package tw.ispan.librarysystem.dto;

import java.time.LocalDateTime;

public class ReservationDTO {
    private Integer reservationId;
    private String status;
    private String bookTitle;
    private LocalDateTime reservationDate;

    public ReservationDTO(Integer reservationId, String status, String bookTitle) {
        this.reservationId = reservationId;
        this.status = status;
        this.bookTitle = bookTitle;
    }

    public ReservationDTO(Integer reservationId, String status, String bookTitle, LocalDateTime reservationDate) {
        this.reservationId = reservationId;
        this.status = status;
        this.bookTitle = bookTitle;
        this.reservationDate = reservationDate;
    }

    // Getters and Setters
    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }
}
