package ru.otus.hw.repositories;


import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    void deleteById(long id);

    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long bookId);
}