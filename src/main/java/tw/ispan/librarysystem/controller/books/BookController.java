package tw.ispan.librarysystem.controller.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.dto.BookDTO;
import tw.ispan.librarysystem.dto.PageResponseDTO;
import tw.ispan.librarysystem.entity.books.BookEntity;
import tw.ispan.librarysystem.service.books.BookService;
import tw.ispan.librarysystem.mapper.BookMapper;
import tw.ispan.librarysystem.dto.SearchCondition;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookMapper bookMapper;
    
    @GetMapping("/simple-search")
    public PageResponseDTO<BookDTO> simpleSearch(
        @RequestParam String field,
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "title") String sortField,
        @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<BookEntity> bookPage = bookService.simpleSearch(field, keyword, pageable);
        return bookMapper.toPageDTO(bookPage);
    }

    @PostMapping("/advanced-search")
    public PageResponseDTO<BookDTO> advancedSearch(
        @RequestBody List<SearchCondition> conditions,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "title") String sortField,
        @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<BookEntity> bookPage = bookService.advancedSearch(conditions, pageable);
        return bookMapper.toPageDTO(bookPage);
    }

    @GetMapping("/{bookId}")
    public BookDTO getBookById(@PathVariable Integer bookId) {
        BookEntity book = bookService.findById(bookId).orElse(null);
        return bookMapper.toDTO(book);
    }
}