package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами книг")
@DataJpaTest
public class JpaAuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        List<Author> actualAuthor = authorRepository.findAll();
        List<Author> expectedAuthor = entityManager.getEntityManager()
                .createQuery("select a from Author a", Author.class).getResultList();
        assertThat(expectedAuthor).isNotEmpty()
                .isEqualTo(actualAuthor);
    }

    @DisplayName("Должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectAuthorById(Author author) {
        Optional<Author> actualAuthor = authorRepository.findById(author.getId());
        Author expectedAuthor = entityManager.find(Author.class, author.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }
}
