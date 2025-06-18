package tw.ispan.librarysystem.dto.feedback;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackSummaryDto {
    private Long id;
    private String name;
    private String subject;
    private String content;
    private String status;
    private String reply;
    private LocalDateTime repliedAt;
    private LocalDateTime createdAt;
}

