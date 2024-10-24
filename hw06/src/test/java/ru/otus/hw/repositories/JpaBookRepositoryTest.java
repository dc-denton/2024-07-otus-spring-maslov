package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class})
public class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Optional<Book> actualBook = bookRepository.findById(1L);
        Book expectedBook = entityManager.find(Book.class, 1);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> actualBooks = bookRepository.findAll();
        List<Book> expectedBooks = entityManager.getEntityManager().createQuery("select b from Book b", Book.class)
                .setHint(FETCH.getKey(),
                        entityManager.getEntityManager().getEntityGraph("book-entity-graph"))
                .getResultList();;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);;
    }

    @DisplayName("Должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book expectedBook = new Book(0, "BookTitle_10500", entityManager.find(Author.class, 1),
                List.of(entityManager.find(Genre.class, 1), entityManager.find(Genre.class, 2)));
        Book returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(entityManager.find(Book.class, returnedBook.getId()))
                .isEqualTo(returnedBook);
    }

    @DisplayName("Должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book expectedBook = new Book(1L, "BookTitle_10500", entityManager.find(Author.class, 2),
                List.of(entityManager.find(Genre.class, 4), entityManager.find(Genre.class, 5)));

        assertThat(entityManager.find(Book.class, expectedBook.getId()))
                .isNotEqualTo(expectedBook);

        Book returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(entityManager.find(Book.class, returnedBook.getId()))
                .isEqualTo(returnedBook);
    }

    @DisplayName("Должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        Book firstBook = entityManager.find(Book.class, 1);
        assertThat(firstBook).isNotNull();
        bookRepository.deleteById(1L);
        Book notFoundBook = entityManager.find(Book.class, 1);
        assertThat(notFoundBook).isNull();
    }
}
