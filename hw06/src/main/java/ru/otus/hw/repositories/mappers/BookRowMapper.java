package ru.otus.hw.repositories.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookRowMapper {

    private final AuthorRowMapper authorMapper;

    private final GenreRowMapper genreMapper;

    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorMapper.toDto(book.getAuthor()),
                book.getGenre().stream()
                        .map(genreMapper::toDto)
                        .toList());
    }
}
