package tw.ispan.librarysystem.service.borrow;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.ispan.librarysystem.entity.books.BookEntity;
import tw.ispan.librarysystem.entity.borrow.Borrow;
import tw.ispan.librarysystem.entity.borrow.Borrow.BorrowStatus;
import tw.ispan.librarysystem.entity.member.Member;
import tw.ispan.librarysystem.repository.books.BookRepository;
import tw.ispan.librarysystem.repository.borrow.BorrowRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private static final Logger logger = LoggerFactory.getLogger(BorrowService.class);
    private static final int MAX_RENEW_COUNT = 2;
    private static final int BORROW_DAYS = 30;
    private static final int RENEW_DAYS = 30;

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Borrow borrowBook(Integer userId, Integer bookId) {
        logger.info("處理借書請求 - 使用者ID: {}, 書籍ID: {}", userId, bookId);

        // 檢查書籍是否存在且可借
        BookEntity book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("找不到該書籍"));

        if (!book.getIsAvailable()) {
            throw new RuntimeException("該書籍目前無法借閱");
        }

        // 檢查是否已借閱該書
        if (borrowRepository.existsActiveBookBorrow(userId, bookId)) {
            throw new RuntimeException("您已經借閱了這本書");
        }

        // 建立借閱記錄
        Borrow borrow = new Borrow();
        borrow.setUserId(userId);
        borrow.setBookId(bookId);
        borrow.setBorrowDate(LocalDateTime.now());
        borrow.setDueDate(LocalDateTime.now().plusDays(BORROW_DAYS));
        borrow.setRenewCount(0);
        borrow.setStatus(BorrowStatus.BORROWED);

        // 更新書籍狀態
        book.setIsAvailable(false);
        bookRepository.save(book);

        // 儲存借閱記錄
        Borrow savedBorrow = borrowRepository.save(borrow);
        logger.info("借書成功 - 借閱ID: {}", savedBorrow.getBorrowId());
        return savedBorrow;
    }

    @Transactional
    public Borrow returnBook(Integer borrowId) {
        logger.info("處理還書請求 - 借閱ID: {}", borrowId);

        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new RuntimeException("找不到該借閱記錄"));

        if (borrow.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("該書籍已經歸還");
        }

        // 更新借閱記錄
        borrow.setReturnDate(LocalDateTime.now());
        borrow.setStatus(BorrowStatus.RETURNED);

        // 更新書籍狀態
        BookEntity book = bookRepository.findById(borrow.getBookId())
            .orElseThrow(() -> new RuntimeException("找不到該書籍"));
        book.setIsAvailable(true);
        bookRepository.save(book);

        // 儲存借閱記錄
        Borrow savedBorrow = borrowRepository.save(borrow);
        logger.info("還書成功 - 借閱ID: {}", savedBorrow.getBorrowId());
        return savedBorrow;
    }

    @Transactional
    public Borrow renewBook(Integer borrowId) {
        logger.info("處理續借請求 - 借閱ID: {}", borrowId);

        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new RuntimeException("找不到該借閱記錄"));

        // 檢查借閱狀態
        if (borrow.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("該書籍已歸還，無法續借");
        }

        if (borrow.getStatus() == BorrowStatus.OVERDUE) {
            throw new RuntimeException("該書籍已逾期，無法續借");
        }

        // 檢查續借次數
        if (borrow.getRenewCount() >= MAX_RENEW_COUNT) {
            throw new RuntimeException("已達到最大續借次數（" + MAX_RENEW_COUNT + "次）");
        }

        // 檢查是否在可續借期間（到期前3天內）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = borrow.getDueDate();
        if (dueDate.minusDays(3).isAfter(now)) {
            throw new RuntimeException("尚未到續借時間（到期前3天內才能續借）");
        }

        // 更新借閱記錄
        borrow.setDueDate(dueDate.plusDays(RENEW_DAYS));
        borrow.setRenewCount(borrow.getRenewCount() + 1);
        borrow.setStatus(BorrowStatus.RENEWED);

        // 儲存借閱記錄
        Borrow savedBorrow = borrowRepository.save(borrow);
        logger.info("續借成功 - 借閱ID: {}", savedBorrow.getBorrowId());
        return savedBorrow;
    }

    @Transactional(readOnly = true)
    public List<Borrow> getMemberBorrowHistory(Integer userId) {
        logger.info("開始獲取會員借閱歷史 - 使用者ID: {}", userId);
        try {
            // 先檢查會員是否存在
            if (userId == null || userId <= 0) {
                logger.error("無效的使用者ID: {}", userId);
                throw new RuntimeException("無效的使用者ID");
            }

            // 使用原生 SQL 查詢來提高效能
            String sql = "SELECT b.*, bk.title, bk.author, m.name " +
                        "FROM borrow_records b " +
                        "LEFT JOIN books bk ON b.book_id = bk.book_id " +
                        "LEFT JOIN members m ON b.user_id = m.user_id " +
                        "WHERE b.user_id = ? " +
                        "ORDER BY b.borrow_date DESC " +
                        "LIMIT 100";
            
            List<Borrow> borrows = jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
                Borrow borrow = new Borrow();
                borrow.setBorrowId(rs.getInt("borrow_id"));
                borrow.setUserId(rs.getInt("user_id"));
                borrow.setBookId(rs.getInt("book_id"));
                borrow.setBorrowDate(rs.getTimestamp("borrow_date").toLocalDateTime());
                borrow.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
                if (rs.getTimestamp("return_date") != null) {
                    borrow.setReturnDate(rs.getTimestamp("return_date").toLocalDateTime());
                }
                borrow.setRenewCount(rs.getInt("renew_count"));
                borrow.setStatus(Borrow.BorrowStatus.valueOf(rs.getString("status")));
                
                // 設置關聯
                BookEntity book = new BookEntity();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                borrow.setBook(book);
                
                Member member = new Member();
                member.setId(rs.getInt("user_id"));
                member.setName(rs.getString("name"));
                borrow.setMember(member);
                
                return borrow;
            });
            
            logger.info("成功獲取 {} 筆借閱記錄", borrows.size());
            return borrows;
        } catch (Exception e) {
            logger.error("獲取借閱歷史時發生錯誤", e);
            throw new RuntimeException("獲取借閱歷史失敗: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<Borrow> getMemberBorrowHistory(Integer userId, Pageable pageable) {
        logger.info("獲取會員借閱歷史（分頁） - 使用者ID: {}", userId);
        return borrowRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Borrow> getMemberCurrentBorrows(Integer userId) {
        logger.info("獲取會員當前借閱 - 使用者ID: {}", userId);
        return borrowRepository.findByUserIdAndStatus(userId, BorrowStatus.BORROWED);
    }

    @Transactional(readOnly = true)
    public List<Borrow> getMemberOverdueBorrows(Integer userId) {
        logger.info("獲取會員逾期借閱 - 使用者ID: {}", userId);
        return borrowRepository.findByUserIdAndStatus(userId, BorrowStatus.OVERDUE);
    }

    @Transactional
    public void checkAndUpdateOverdueBorrows() {
        logger.info("檢查並更新逾期借閱");
        LocalDateTime now = LocalDateTime.now();
        List<Borrow> overdueBorrows = borrowRepository.findOverdueBorrows(now);
        
        for (Borrow borrow : overdueBorrows) {
            borrow.setStatus(BorrowStatus.OVERDUE);
            borrowRepository.save(borrow);
            logger.info("更新逾期狀態 - 借閱ID: {}", borrow.getBorrowId());
        }
    }

    @Transactional(readOnly = true)
    public boolean canRenew(Integer borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
            .orElseThrow(() -> new RuntimeException("找不到該借閱記錄"));

        return borrow.getStatus() != BorrowStatus.RETURNED &&
               borrow.getStatus() != BorrowStatus.OVERDUE &&
               borrow.getRenewCount() < MAX_RENEW_COUNT &&
               !borrow.getDueDate().minusDays(3).isAfter(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public long getBorrowCount() {
        return borrowRepository.count();
    }
} 