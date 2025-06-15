package tw.ispan.librarysystem.service.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.ispan.librarysystem.dto.feedback.FeedbackReplyDto;
import tw.ispan.librarysystem.entity.feedback.Feedback;
import tw.ispan.librarysystem.repository.feedback.FeedbackRepository;
import tw.ispan.librarysystem.service.feedback.FeedbackService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public boolean replyToFeedback(FeedbackReplyDto dto) {
        Optional<Feedback> optional = feedbackRepository.findById(dto.getId());
        if (optional.isPresent()) {
            Feedback feedback = optional.get();
            feedback.setReply(dto.getReply());
            feedback.setStatus(dto.getStatus());
            feedback.setRepliedAt(LocalDateTime.now());
            feedbackRepository.save(feedback);
            return true;
        }
        return false;
    }
}
