package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@Import({JpaCommentRepository.class})
public class JpaCommentRepositoryTest {

    @Autowired
    JpaCommentRepository commentRepository;

    List<Comment> dbComments;

    List<Author> dbAuthors;

    List<Genre> dbGenres;

    List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbComments = getDbComments(dbBooks);
    }

    @DisplayName("Должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentById(Comment comment) {
        Optional<Comment> actualComment = commentRepository.findById(comment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(comment);
    }

    @Test
    @DisplayName("Должен обновлять комментарий")
    void shouldUpdateComment() {
        Comment expectedComment = new Comment(1L, "editedComment", dbBooks.get(1));

        assertThat(commentRepository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        Comment returnedBook = commentRepository.save(expectedComment);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @Test
    @DisplayName("Должен сохранять комментарий")
    void shouldSaveComment() {
        Comment expectedComment = new Comment(0, "newComment", dbBooks.get(0));
        Comment returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    private static List<Comment> getDbComments(List<Book> books) {
        return List.of(new Comment(1, "text_1", books.get(0)),
                new Comment(2, "text_2", books.get(0)),
                new Comment(3, "text_3", books.get(0)),
                new Comment(4, "text_4", books.get(1)),
                new Comment(5, "text_5", books.get(1)),
                new Comment(6, "text_6", books.get(2))
        );
    }

    private static List<Comment> getDbComments() {
        List<Book> books = getDbBooks(getDbAuthors(), getDbGenres());
        return getDbComments(books);
    }

    private static List<Book> getDbBooks() {
        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }


    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}
