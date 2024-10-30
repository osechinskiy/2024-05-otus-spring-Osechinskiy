package ru.otus.hw.controller;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@Controller
@AllArgsConstructor
@Log4j2
public class CommentController {

    private static final String ERROR_TEXT = "delay call failed error:{}";

    private final BookService bookService;

    private final CommentService commentService;

    private final CircuitBreaker circuitBreaker;

    @GetMapping("/comment/{bookId}")
    public String comment(@PathVariable long bookId, Model model) {
        Book book = getBook(bookId);
        List<Comment> comments = getCommentsByBook(bookId);

        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "comment/comment";
    }

    private Book getBook(long bookId) {
        return circuitBreaker.run(() -> bookService.findById(bookId),
                        t -> {
                            logError(t.getMessage());
                            return null;
                        });
    }

    private List<Comment> getCommentsByBook(long bookId) {
        return circuitBreaker.run(() -> commentService.findByBookId(bookId),
                        t -> {
                            logError(t.getMessage());
                            return Collections.emptyList();
                        });
    }

    private void logError(String message) {
        log.error(ERROR_TEXT, message);
    }
}
