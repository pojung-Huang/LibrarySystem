package tw.ispan.librarysystem.service.borrow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.ispan.librarysystem.entity.borrow.Borrow;
import tw.ispan.librarysystem.entity.books.BookEntity;
import tw.ispan.librarysystem.repository.borrow.BorrowRepository;
import tw.ispan.librarysystem.repository.books.BookRepository;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.math.BigInteger;

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Borrow borrowBook(Integer memberId, Integer bookId) {
        // 檢查書籍是否可借
        BookEntity book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("找不到該書籍"));
        
        if (!book.getIsAvailable()) {
            throw new RuntimeException("該書籍目前無法借閱");
        }

        // 檢查會員是否已有借閱該書
        List<Borrow> existingBorrows = borrowRepository.findByMemberId(memberId);
        for (Borrow borrow : existingBorrows) {
            if (borrow.getBookId().equals(bookId) && 
                (borrow.getStatus().equals("BORROWED") || borrow.getStatus().equals("RENEWED"))) {
                throw new RuntimeException("您已經借閱了這本書");
            }
        }

        // 建立借閱記錄
        Borrow borrow = new Borrow();
        borrow.setMemberId(memberId);
        borrow.setBookId(bookId);
        borrow.setBorrowDate(new Date());
        
        // 設定歸還日期（預設14天）
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        borrow.setDueDate(calendar.getTime());
        
        borrow.setRenewCount(0);
        borrow.setStatus("BORROWED");
        borrow.setCreatedAt(new Date());
        borrow.setUpdatedAt(new Date());

        // 更新書籍狀態
        book.setIsAvailable(false);
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }

    @Transactional
    public Borrow returnBook(BigInteger borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new RuntimeException("找不到該借閱記錄"));

        if (borrow.getStatus().equals("RETURNED")) {
            throw new RuntimeException("該書籍已經歸還");
        }

        // 更新借閱記錄
        borrow.setReturnDate(new Date());
        borrow.setStatus("RETURNED");
        borrow.setUpdatedAt(new Date());

        // 更新書籍狀態
        BookEntity book = bookRepository.findById(borrow.getBookId())
            .orElseThrow(() -> new RuntimeException("找不到該書籍"));
        book.setIsAvailable(true);
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }

    @Transactional
    public Borrow renewBook(BigInteger borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new RuntimeException("找不到該借閱記錄"));

        // 檢查借閱狀態
        if (!borrow.getStatus().equals("BORROWED") && !borrow.getStatus().equals("RENEWED")) {
            throw new RuntimeException("該書籍無法續借，因為它已經被歸還");
        }

        // 檢查是否已達最大續借次數
        if (borrow.getRenewCount() >= 2) {
            throw new RuntimeException("已達到最大續借次數（2次）");
        }

        // 檢查是否已逾期
        if (borrow.getDueDate().before(new Date())) {
            throw new RuntimeException("該書籍已逾期，無法續借");
        }

        // 檢查是否有其他會員預約
        BookEntity book = bookRepository.findById(borrow.getBookId())
            .orElseThrow(() -> new RuntimeException("找不到該書籍"));
        
        // 這裡可以添加檢查是否有其他會員預約的邏輯
        // 如果有預約系統的話

        // 更新借閱記錄
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrow.getDueDate());
        calendar.add(Calendar.DAY_OF_MONTH, 14); // 延長14天
        borrow.setDueDate(calendar.getTime());
        borrow.setRenewCount(borrow.getRenewCount() + 1);
        borrow.setStatus("RENEWED");
        borrow.setUpdatedAt(new Date());

        return borrowRepository.save(borrow);
    }

    // 新增：檢查是否可以續借
    public boolean canRenew(BigInteger borrowId) {
        try {
            Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("找不到該借閱記錄"));

            // 檢查借閱狀態
            if (!borrow.getStatus().equals("BORROWED") && !borrow.getStatus().equals("RENEWED")) {
                return false;
            }

            // 檢查是否已達最大續借次數
            if (borrow.getRenewCount() >= 2) {
                return false;
            }

            // 檢查是否已逾期
            if (borrow.getDueDate().before(new Date())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 新增：獲取續借資訊
    public RenewInfo getRenewInfo(BigInteger borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new RuntimeException("找不到該借閱記錄"));

        RenewInfo info = new RenewInfo();
        info.setBorrowId(borrow.getBorrowId());
        info.setBookId(borrow.getBookId());
        info.setCurrentDueDate(borrow.getDueDate());
        info.setRenewCount(borrow.getRenewCount());
        info.setRemainingRenews(2 - borrow.getRenewCount());
        info.setCanRenew(canRenew(borrowId));

        // 計算新的到期日
        if (info.isCanRenew()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(borrow.getDueDate());
            calendar.add(Calendar.DAY_OF_MONTH, 14);
            info.setNewDueDate(calendar.getTime());
        }

        return info;
    }

    // 續借資訊類別
    public static class RenewInfo {
        private Integer borrowId;
        private Integer bookId;
        private Date currentDueDate;
        private Date newDueDate;
        private Integer renewCount;
        private Integer remainingRenews;
        private boolean canRenew;

        // Getters and Setters
        public Integer getBorrowId() { return borrowId; }
        public void setBorrowId(Integer borrowId) { this.borrowId = borrowId; }
        
        public Integer getBookId() { return bookId; }
        public void setBookId(Integer bookId) { this.bookId = bookId; }
        
        public Date getCurrentDueDate() { return currentDueDate; }
        public void setCurrentDueDate(Date currentDueDate) { this.currentDueDate = currentDueDate; }
        
        public Date getNewDueDate() { return newDueDate; }
        public void setNewDueDate(Date newDueDate) { this.newDueDate = newDueDate; }
        
        public Integer getRenewCount() { return renewCount; }
        public void setRenewCount(Integer renewCount) { this.renewCount = renewCount; }
        
        public Integer getRemainingRenews() { return remainingRenews; }
        public void setRemainingRenews(Integer remainingRenews) { this.remainingRenews = remainingRenews; }
        
        public boolean isCanRenew() { return canRenew; }
        public void setCanRenew(boolean canRenew) { this.canRenew = canRenew; }
    }

    public List<Borrow> getMemberBorrows(Integer memberId) {
        return borrowRepository.findByMemberId(memberId);
    }

    public List<Borrow> getBookBorrows(Integer bookId) {
        return borrowRepository.findByBookId(bookId);
    }

    public List<Borrow> getMemberBorrowHistory(Integer memberId) {
        return borrowRepository.findMemberBorrowHistory(memberId);
    }

    public List<Borrow> getMemberCurrentBorrows(Integer memberId) {
        return borrowRepository.findMemberBorrowsByStatus(memberId, "BORROWED");
    }

    public List<Borrow> getMemberRenewedBorrows(Integer memberId) {
        return borrowRepository.findMemberBorrowsByStatus(memberId, "RENEWED");
    }

    public List<Borrow> getMemberReturnedBorrows(Integer memberId) {
        return borrowRepository.findMemberBorrowsByStatus(memberId, "RETURNED");
    }

    public long getBorrowCount() {
        return borrowRepository.count();
    }
} 