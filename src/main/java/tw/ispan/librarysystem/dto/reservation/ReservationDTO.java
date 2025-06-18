package tw.ispan.librarysystem.dto.reservation;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    @JsonProperty("reservation_id")
    private Integer reservationId;
    
    @JsonProperty("user_id")
    private Integer userId;
    
    @JsonProperty("reservation_date")
    private LocalDateTime reservationDate;
    
    @JsonProperty("expiry_date")
    private LocalDateTime expiryDate;
    
    private String status;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    // Book information
    @JsonProperty("book_id")
    private Integer bookId;
    
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String classification;
    
    @JsonProperty("category_name")
    private String categoryName;

    // 新增取書相關欄位
    @JsonProperty("pickup_location")
    private String pickupLocation;
    
    @JsonProperty("pickup_time")
    private LocalDateTime pickupTime;

    // ✅ 新增這段 constructor 給 JPQL 使用
    public ReservationDTO(Integer reservationId, String status, String title, LocalDateTime reservationDate) {
        this.reservationId = reservationId;
        this.status = status;
        this.title = title;
        this.reservationDate = reservationDate;
    }
}
