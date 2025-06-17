package tw.ispan.librarysystem.controller.borrow;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.entity.borrow.Borrow;
import tw.ispan.librarysystem.service.borrow.BorrowService;
import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class BorrowController {
    private static final Logger logger = LoggerFactory.getLogger(BorrowController.class);
    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/return/{borrowId}")
    public ResponseEntity<?> returnBook(@PathVariable Integer borrowId) {
        try {
            logger.info("收到還書請求 - 借閱ID: {}", borrowId);
            Borrow borrow = borrowService.returnBook(borrowId);
            return ResponseEntity.ok(borrow);
        } catch (RuntimeException e) {
            logger.error("還書失敗", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/renew/{borrowId}")
    public ResponseEntity<?> renewBook(@PathVariable Integer borrowId) {
        try {
            logger.info("收到續借請求 - 借閱ID: {}", borrowId);
            Borrow borrow = borrowService.renewBook(borrowId);
            return ResponseEntity.ok(borrow);
        } catch (RuntimeException e) {
            logger.error("續借失敗", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/member/{userId}/history")
    public ResponseEntity<?> getMemberBorrowHistory(
        @PathVariable Integer userId,
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean withPagination
    ) {
        try {
            logger.info("收到獲取會員借閱歷史請求 - 使用者ID: {}", userId);
            if (withPagination) {
                Page<Borrow> borrows = borrowService.getMemberBorrowHistory(userId, pageable);
                return ResponseEntity.ok(borrows);
            } else {
                List<Borrow> borrows = borrowService.getMemberBorrowHistory(userId);
                return ResponseEntity.ok(borrows);
            }
        } catch (Exception e) {
            logger.error("獲取借閱歷史失敗", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/member/{userId}/current")
    public ResponseEntity<?> getMemberCurrentBorrows(@PathVariable Integer userId) {
        try {
            logger.info("收到獲取會員當前借閱請求 - 使用者ID: {}", userId);
            List<Borrow> borrows = borrowService.getMemberCurrentBorrows(userId);
            return ResponseEntity.ok(borrows);
        } catch (Exception e) {
            logger.error("獲取當前借閱失敗", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/member/{userId}/overdue")
    public ResponseEntity<?> getMemberOverdueBorrows(@PathVariable Integer userId) {
        try {
            logger.info("收到獲取會員逾期借閱請求 - 使用者ID: {}", userId);
            List<Borrow> borrows = borrowService.getMemberOverdueBorrows(userId);
            return ResponseEntity.ok(borrows);
        } catch (Exception e) {
            logger.error("獲取逾期借閱失敗", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/renew/{borrowId}/check")
    public ResponseEntity<?> checkRenewable(@PathVariable Integer borrowId) {
        try {
            logger.info("收到檢查是否可續借請求 - 借閱ID: {}", borrowId);
            boolean canRenew = borrowService.canRenew(borrowId);
            return ResponseEntity.ok(canRenew);
        } catch (RuntimeException e) {
            logger.error("檢查續借資格失敗", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 