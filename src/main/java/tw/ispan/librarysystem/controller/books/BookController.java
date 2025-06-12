package tw.ispan.librarysystem.controller.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.entity.books.BookEntity;
import tw.ispan.librarysystem.repository.books.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    private BookRepository bookRepository;
    
    @GetMapping("/search")
    public Page<BookEntity> searchBooks(
        @RequestParam(required = false) String title,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "title,asc") String[] sort
    ) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        if (title != null && !title.trim().isEmpty()) {
            return bookRepository.searchBooks(
                title, null, null, null, null, null, pageable
            );
        }
        
        return bookRepository.findAll(pageable);
    }

    @GetMapping("/{bookId}")
    public BookEntity getBookById(@PathVariable Integer bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }
}