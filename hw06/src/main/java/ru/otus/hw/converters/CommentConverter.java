package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;

@Component
public class CommentConverter {

    public String commentToString(CommentDto commentDto) {
        return "Book: %s, Comment: %s, Id: %s"
                .formatted(commentDto.getBookTitle(), commentDto.getText(), commentDto.getId());
    }
}
