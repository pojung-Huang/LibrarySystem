package tw.ispan.librarysystem.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import tw.ispan.librarysystem.dto.BookDTO;
import tw.ispan.librarysystem.dto.PageResponseDTO;
import tw.ispan.librarysystem.entity.books.BookEntity;

@Component
public class BookMapper {
    
    public BookDTO toDTO(BookEntity entity) {
        if (entity == null) {
            return null;
        }

        BookDTO dto = new BookDTO();
        dto.setBookId(entity.getBookId());
        dto.setIsbn(entity.getIsbn());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setPublisher(entity.getPublisher());
        dto.setPublishdate(entity.getPublishdate());
        dto.setVersion(entity.getVersion());
        dto.setType(entity.getType());
        dto.setLanguage(entity.getLanguage());
        dto.setIsAvailable(entity.getIsAvailable());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setClassification(entity.getClassification());
        
        
        
        if (entity.getCategory() != null) {
            dto.setcId(entity.getCategory().getcId());
        }
        
        if (entity.getCategory() != null && entity.getCategory().getCategorysystem() != null) {
            dto.setCategorysystem(entity.getCategory().getCategorysystem().getCsName());
        }
        
        dto.setSummary(entity.getBookDetail() != null ? entity.getBookDetail().getSummary() : null);
        dto.setImgUrl(entity.getBookDetail() != null ? entity.getBookDetail().getImgUrl() : null);
        
        return dto;
    }

    public List<BookDTO> toDTOList(List<BookEntity> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PageResponseDTO<BookDTO> toPageDTO(Page<BookEntity> entityPage) {
        List<BookDTO> dtos = toDTOList(entityPage.getContent());
        return new PageResponseDTO<>(
            dtos,
            entityPage.getNumber(),
            entityPage.getSize(),
            entityPage.getTotalElements(),
            entityPage.getTotalPages(),
            entityPage.isLast(),
            entityPage.isFirst()
        );
    }
} 