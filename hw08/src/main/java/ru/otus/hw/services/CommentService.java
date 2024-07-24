package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import ru.otus.hw.models.Comment;

public interface CommentService {
    Optional<Comment> findById(String id);

    List<Comment> findByBookId(String bookId);

    Comment insert(String text, String bookId);

    Comment update(String id, String text, String bookId);

    void deleteById(String id);
}
