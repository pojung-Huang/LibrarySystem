package tw.ispan.librarysystem.repository.violation;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.ispan.librarysystem.entity.violation.ViolationRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface ViolationRecordRepository extends JpaRepository<ViolationRecord, Integer> {
    List<ViolationRecord> findByUserId(Integer userId);
    long countByUserId(Integer userId);
    boolean existsByUserIdAndViolationTypeAndViolationDateBetween(Integer userId, String violationType, LocalDateTime start, LocalDateTime end);
    boolean existsByUserIdAndPenaltyEndDateAfter(Integer userId, LocalDateTime now);
} 