package tw.ispan.librarysystem.entity.feedback;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = true) // 可為 null
    private String cardNumber;

    private String phone;
    private String email;
    private String subject;

    @Column(length = 1000)
    private String content;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 1000)
    private String reply;

    private LocalDateTime repliedAt;

    @Column(length = 20)
    private String status = "待處理";



}
