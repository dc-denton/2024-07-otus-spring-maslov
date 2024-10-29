package ru.otus.hw.repositories.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentRowMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(), comment.getText(), comment.getBook().getId(), comment.getBook().getTitle());
    }
}
