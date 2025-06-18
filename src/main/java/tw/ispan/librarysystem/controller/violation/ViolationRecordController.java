package tw.ispan.librarysystem.controller.violation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.entity.violation.ViolationRecord;
import tw.ispan.librarysystem.service.violation.ViolationRecordService;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
@CrossOrigin(origins = "http://localhost:8080")
public class ViolationRecordController {

    @Autowired
    private ViolationRecordService service;

    @GetMapping("/user/{userId}")
    public List<ViolationRecord> getUserViolations(@PathVariable Integer userId) {
        return service.getViolationsByUserId(userId);
    }

    @GetMapping("/user/{userId}/suspended")
    public boolean isUserSuspended(@PathVariable Integer userId) {
        return service.isUserSuspended(userId);
    }
} 