package tw.ispan.librarysystem.repository.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.ispan.librarysystem.entity.feedback.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // 可擴充自定查詢方法，例如：List<Feedback> findByEmail(String email);
}