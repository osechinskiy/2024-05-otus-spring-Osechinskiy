package ru.otus.hw.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.rest.dto.CommentResponse;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@RestController
@AllArgsConstructor
public class CommentController {

    private final BookService bookService;

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping("/api/v1/comment/{id}")
    public CommentResponse comment(@PathVariable long id) {
        Book book = bookService.findById(id);
        List<Comment> comments = commentService.findByBookId(id);
        return new CommentResponse(book.getId(), book.getTitle(), book.getAuthor().getFullName(),
                comments.stream().map(commentMapper::toDto).toList());
    }
}
