package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class})
public class JpaBookRepositoryTest {

    @Autowired
    JpaBookRepository bookRepository;

    @Autowired
    TestEntityManager entityManager;

    List<Author> dbAuthors;

    List<Genre> dbGenres;

    List<Book> dbBooks;

    final long FIRST_BOOK_ID = 1L;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("Должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book book) {
        Optional<Book> actualBook = bookRepository.findById(book.getId());
        Book expectedBook = entityManager.find(Book.class, book.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> actualBooks = bookRepository.findAll();
        List<Book> expectedBooks = entityManager.getEntityManager().createQuery("select b from Book b", Book.class)
                .setHint("jakarta.persistence.fetchgraph"
                        , entityManager.getEntityManager().getEntityGraph("book-entity-graph"))
                .getResultList();;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);;
    }

    @DisplayName("Должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        Book returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        Book returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        Book firstBook = entityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(firstBook).isNotNull();
        bookRepository.deleteById(FIRST_BOOK_ID);
        Book notFoundBook = entityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(notFoundBook).isNull();
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
    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }
    private static List<Book> getDbBooks() {
        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
