package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@Import({JpaCommentRepository.class})
public class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository commentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        Optional<Comment> actualComment = commentRepository.findById(1L);
        Comment expectedComment = entityManager.find(Comment.class, 1);
        assertThat(actualComment).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("Должен обновлять комментарий")
    void shouldUpdateComment() {
        Comment expectedComment = new Comment(1L, "editedComment", entityManager.find(Book.class, 1));

        assertThat(entityManager.find(Comment.class, expectedComment.getId()))
                .isNotEqualTo(expectedComment);

        Comment returnedBook = commentRepository.save(expectedComment);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(entityManager.find(Comment.class, returnedBook.getId()))
                .isEqualTo(returnedBook);
    }

    @Test
    @DisplayName("Должен сохранять комментарий")
    void shouldSaveComment() {
        Comment expectedComment = new Comment(0, "newComment", entityManager.find(Book.class, 1));
        Comment returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(entityManager.find(Comment.class, returnedComment.getId()))
                .isEqualTo(returnedComment);
    }

    @Test
    @DisplayName("Должен удалять комментарий")
    void shouldDeleteComment() {
        Comment comment = entityManager.find(Comment.class, 1);
        assertThat(comment).isNotNull();
        commentRepository.deleteById(1L);
        Comment notFoundComment = entityManager.find(Comment.class, 1);
        assertThat(notFoundComment).isNull();
    }
}
