package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами книг")
@DataJpaTest
@Import({JpaGenreRepository.class})
public class JpaGenresRepositoryTest {

    @Autowired
    private JpaGenreRepository genreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("Должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectAuthorsList() {
        List<Genre> actualBooks = genreRepository.findAll();
        List<Genre> expectedBooks = dbGenres;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("Должен загружать список всех жанров по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenresByIds(Genre expectedGenre) {
        List<Genre> actualGenres = genreRepository.findAllByIds(Set.of(expectedGenre.getId()));
        for (Genre actualGenre : actualGenres) {
            assertThat(actualGenre)
                    .isEqualTo(expectedGenre);
        }
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}