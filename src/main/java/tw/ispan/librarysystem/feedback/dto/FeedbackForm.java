package tw.ispan.librarysystem.feedback.dto;

import lombok.Data;

@Data
public class FeedbackForm {
    private String name;
    private String cardNumber;
    private String phone;
    private String email;
    private String subject;
    private String content;
    private String captcha;
}
