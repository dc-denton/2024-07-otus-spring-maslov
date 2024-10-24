package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.mappers.CommentRowMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями")
@DataJpaTest
@Import({JpaCommentRepository.class, CommentServiceImpl.class
        , JpaBookRepository.class, JpaCommentRepository.class, CommentRowMapper.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Должен загружать комментарий по id")
    void shouldReturnCorrectCommentById() {
        Optional<CommentDto> actualComment = commentService.findById(1L);
        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Должен обновлять комментарий")
    void shouldUpdateComment() {
        Optional<CommentDto> comment = commentService.findById(1L);

        CommentDto expectedComment = commentService.update(comment.get().getId(), "New comment", 1L);

        Optional<CommentDto> returnedBook = commentService.findById(1L);
        assertThat(expectedComment).isNotNull();

        assertThat(expectedComment.getId()).isEqualTo(returnedBook.get().getId());
    }

    @Test
    @DisplayName("Должен сохранять комментарий")
    void shouldSaveComment() {
        CommentDto expectedComment = commentService.insert("New comment", 1L);
        Optional<CommentDto> returnedComment = commentService.findById(expectedComment.getId());

        assertThat(returnedComment).isNotNull();
        assertThat(expectedComment.getId()).isEqualTo(returnedComment.get().getId());
    }
}
