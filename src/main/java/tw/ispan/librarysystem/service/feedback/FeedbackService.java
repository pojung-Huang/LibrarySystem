package tw.ispan.librarysystem.service.feedback;

import tw.ispan.librarysystem.dto.feedback.FeedbackReplyDto;

public interface FeedbackService {
    boolean replyToFeedback(FeedbackReplyDto dto);
}
