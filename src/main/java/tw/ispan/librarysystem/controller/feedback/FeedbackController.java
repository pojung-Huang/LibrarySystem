package tw.ispan.librarysystem.controller.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.dto.feedback.FeedbackFormDto;

import jakarta.servlet.http.HttpSession;
import tw.ispan.librarysystem.entity.feedback.Feedback;
import tw.ispan.librarysystem.repository.feedback.FeedbackRepository;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // ✅ 根據 Nuxt 前端實際網址修改
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PostMapping
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackFormDto form, HttpSession session) {
        // 驗證驗證碼
        String expectedCaptcha = (String) session.getAttribute("captcha");
        if (expectedCaptcha == null || !expectedCaptcha.equalsIgnoreCase(form.getCaptcha())) {
            System.out.println("驗證失敗❌");
            System.out.println("使用者輸入的驗證碼：" + form.getCaptcha());
            System.out.println("Session 中的正確驗證碼：" + expectedCaptcha);
            return ResponseEntity.badRequest().body("驗證碼錯誤");
        }

        // 建立 Entity 並儲存
        Feedback feedback = new Feedback();
        feedback.setName(form.getName());
        feedback.setCardNumber(form.getCardNumber());
        feedback.setPhone(form.getPhone());
        feedback.setEmail(form.getEmail());
        feedback.setSubject(form.getSubject());
        feedback.setContent(form.getContent());

        feedbackRepository.save(feedback);

        return ResponseEntity.ok("留言已成功送出");
    }
}
