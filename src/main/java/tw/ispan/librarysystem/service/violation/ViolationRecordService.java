package tw.ispan.librarysystem.service.violation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.ispan.librarysystem.entity.violation.ViolationRecord;
import tw.ispan.librarysystem.repository.violation.ViolationRecordRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ViolationRecordService {

    @Autowired
    private ViolationRecordRepository repository;

    public ViolationRecord addViolation(Integer userId, String violationType) {
        long count = repository.countByUserId(userId);

        int days = 180; // 借閱違規預設懲罰天數
        if (violationType.equals("預約了沒拿") && count >= 2) {
            days = 60; // 預約違規滿 3 次，懲罰 60 天
        }

        ViolationRecord record = new ViolationRecord();
        record.setUserId(userId);
        record.setViolationType(violationType);
        record.setViolationDate(LocalDateTime.now());
        record.setPenaltyEndDate(LocalDateTime.now().plusDays(days));
        return repository.save(record);
    }

    public List<ViolationRecord> getViolationsByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    public boolean isUserSuspended(Integer userId) {
        return repository.existsByUserIdAndPenaltyEndDateAfter(userId, LocalDateTime.now());
    }
} 