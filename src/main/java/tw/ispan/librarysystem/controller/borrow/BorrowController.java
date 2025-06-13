package tw.ispan.librarysystem.controller.borrow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.entity.borrow.Borrow;
import tw.ispan.librarysystem.service.borrow.BorrowService;
import tw.ispan.librarysystem.service.borrow.BorrowService.RenewInfo;
import java.util.List;
import java.math.BigInteger;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = "http://localhost:3000")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

     // 測試API是否正常運作
    @GetMapping("/test")
    public String test() {
        return "Borrow API is working!";
    }

    // 測試資料庫連線
    @GetMapping("/test/db")
    public ResponseEntity<?> testDatabase() {
        try {
            // 嘗試取得所有借閱紀錄數量
            long count = borrowService.getBorrowCount();
            return ResponseEntity.ok("Database connected! Total borrow records: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Database error: " + e.getMessage());
        }
    }


    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(
        @RequestParam Integer memberId,
        @RequestParam Integer bookId
    ) {
        try {
            Borrow borrow = borrowService.borrowBook(memberId, bookId);
            return ResponseEntity.ok(borrow);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{borrowId}")
    public ResponseEntity<?> returnBook(@PathVariable BigInteger borrowId) {
        try {
            Borrow borrow = borrowService.returnBook(borrowId);
            return ResponseEntity.ok(borrow);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/renew/{borrowId}")
    public ResponseEntity<?> renewBook(@PathVariable BigInteger borrowId) {
        try {
            Borrow borrow = borrowService.renewBook(borrowId);
            return ResponseEntity.ok(borrow);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Borrow>> getMemberBorrows(@PathVariable Integer memberId) {
        List<Borrow> borrows = borrowService.getMemberBorrows(memberId);
        return ResponseEntity.ok(borrows);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Borrow>> getBookBorrows(@PathVariable Integer bookId) {
        List<Borrow> borrows = borrowService.getBookBorrows(bookId);
        return ResponseEntity.ok(borrows);
    }

    @GetMapping("/member/{memberId}/history")
    public ResponseEntity<List<Borrow>> getMemberBorrowHistory(@PathVariable Integer memberId) {
        List<Borrow> borrows = borrowService.getMemberBorrowHistory(memberId);
        return ResponseEntity.ok(borrows);
    }

    @GetMapping("/member/{memberId}/current")
    public ResponseEntity<List<Borrow>> getMemberCurrentBorrows(@PathVariable Integer memberId) {
        List<Borrow> borrows = borrowService.getMemberCurrentBorrows(memberId);
        return ResponseEntity.ok(borrows);
    }

    @GetMapping("/member/{memberId}/renewed")
    public ResponseEntity<List<Borrow>> getMemberRenewedBorrows(@PathVariable Integer memberId) {
        List<Borrow> borrows = borrowService.getMemberRenewedBorrows(memberId);
        return ResponseEntity.ok(borrows);
    }

    @GetMapping("/member/{memberId}/returned")
    public ResponseEntity<List<Borrow>> getMemberReturnedBorrows(@PathVariable Integer memberId) {
        List<Borrow> borrows = borrowService.getMemberReturnedBorrows(memberId);
        return ResponseEntity.ok(borrows);
    }

    @GetMapping("/renew/{borrowId}/check")
    public ResponseEntity<?> checkRenewable(@PathVariable BigInteger borrowId) {
        try {
            boolean canRenew = borrowService.canRenew(borrowId);
            return ResponseEntity.ok(canRenew);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/renew/{borrowId}/info")
    public ResponseEntity<?> getRenewInfo(@PathVariable BigInteger borrowId) {
        try {
            RenewInfo info = borrowService.getRenewInfo(borrowId);
            return ResponseEntity.ok(info);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 