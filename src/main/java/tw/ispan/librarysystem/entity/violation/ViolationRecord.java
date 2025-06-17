package tw.ispan.librarysystem.entity.violation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "violation_records")
public class ViolationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer violationId;

    @Column(nullable = false)
    private Integer userId;

    @Column(length = 20, nullable = false)
    private String violationType;

    @Column(nullable = false)
    private LocalDateTime violationDate;

    @Column(name = "penalty_end_date")
    private LocalDateTime penaltyEndDate;

    public Integer getViolationId() { return violationId; }
    public void setViolationId(Integer violationId) { this.violationId = violationId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getViolationType() { return violationType; }
    public void setViolationType(String violationType) { this.violationType = violationType; }

    public LocalDateTime getViolationDate() { return violationDate; }
    public void setViolationDate(LocalDateTime violationDate) { this.violationDate = violationDate; }

    public LocalDateTime getPenaltyEndDate() { return penaltyEndDate; }
    public void setPenaltyEndDate(LocalDateTime penaltyEndDate) { this.penaltyEndDate = penaltyEndDate; }
} 