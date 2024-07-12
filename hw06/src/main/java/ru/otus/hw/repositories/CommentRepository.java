package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import ru.otus.hw.models.Comment;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List<Comment> findByBookId(long bookId);

    Comment save(Comment comment);

    void deleteById(Long id);
}
