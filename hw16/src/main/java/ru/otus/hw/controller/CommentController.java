package ru.otus.hw.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@Controller
@AllArgsConstructor
public class CommentController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping("/comment/{bookId}")
    public String comment(@PathVariable long bookId, Model model) {
        var book = bookService.findById(bookId);
        var comments = commentService.findByBookId(bookId);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "comment/comment";
    }
}
