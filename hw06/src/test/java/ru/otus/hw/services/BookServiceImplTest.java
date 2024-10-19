package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import ru.otus.hw.repositories.mappers.AuthorRowMapper;
import ru.otus.hw.repositories.mappers.BookRowMapper;
import ru.otus.hw.repositories.mappers.GenreRowMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Сервис для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class, JpaAuthorRepository.class
        , BookRowMapper.class, AuthorRowMapper.class, GenreRowMapper.class, BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("Должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(BookDto expectedBook) {
        Optional<BookDto> returnedBook = bookService.findById(expectedBook.getId());

        assertThat(returnedBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<BookDto> returnedBooks = bookService.findAll();

        assertFalse(returnedBooks.isEmpty());
        assertEquals(3, returnedBooks.size());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @DisplayName("Должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        BookDto expectedBook = new BookDto(4L, "newBook", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(1)));

        BookDto returnedBookDto = bookService.insert("newBook", dbAuthors.get(0).getId(),
                Set.of(dbGenres.get(0).getId(), dbGenres.get(1).getId()));

        assertThat(returnedBookDto).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    @DisplayName("Должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        BookDto expectedBook = new BookDto(1L, "editedBook", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(1)));


        BookDto returnedBook = bookService.update(1L, "editedBook", dbAuthors.get(0).getId(),
                Set.of(dbGenres.get(0).getId(), dbGenres.get(1).getId()));

        assertThat(returnedBook).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
    }

    @DisplayName("Должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        BookDto book = bookService.insert("newBook", 1L, Set.of(1L, 2L));
        bookService.deleteById(book.getId());
        Optional<BookDto> actualBook = bookService.findById(book.getId());
        assertThat(actualBook).isEmpty();
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dbAuthors.get((id - 1)),
                        dbGenres.subList(((id - 1) * 2), ((id - 1) * 2 + 2))
                ))
                .toList();
    }

    private static List<BookDto> getDbBooks() {
        List<AuthorDto> dbAuthors = getDbAuthors();
        List<GenreDto> dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}