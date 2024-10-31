package ru.otus.hw.controller;

import jakarta.validation.Valid;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class BookController {

    private static final String ERROR_TEXT = "delay call failed error:{}";

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookMapper bookMapper;

    private final CircuitBreaker circuitBreaker;

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/book/all";
    }

    @GetMapping("/book/all")
    public String getAllBooks(Model model) {
        List<Book> books = circuitBreaker.run(bookService::findAll,
                t -> {
                    logError(t.getMessage());
                    return Collections.emptyList();
                });

        model.addAttribute("books", books);
        return "book/book";
    }

    @GetMapping("book/create")
    public String createBook(Model model) {
        BookDto book = new BookDto(null, null, null, null);
        List<Author> authors = getAllAuthors();
        List<Genre> genres = getAllGenres();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @PostMapping("/book/create")
    public String createBook(@Valid @ModelAttribute("book") BookDto book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Author> authors = getAllAuthors();
            List<Genre> genres = getAllGenres();

            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "book/edit";
        }

        bookService.insert(book);
        return "redirect:/book/all";
    }

    @PostMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/book/all";
    }

    @GetMapping("/book/edit/{id}")
    public String editBook(@PathVariable long id, Model model) {
        Book book = circuitBreaker.run(() -> bookService.findById(id),
                t -> {
                    logError(t.getMessage());
                    return new Book();
                });
        List<Author> authors = getAllAuthors();
        List<Genre> genres = getAllGenres();

        model.addAttribute("book", bookMapper.toDto(book));
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @PostMapping("/book/edit")
    public String saveBook(@Valid @ModelAttribute("book") BookDto book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "book/edit";
        }

        bookService.update(book);
        return "redirect:/book/all";
    }
    
    private List<Author> getAllAuthors() {
        return circuitBreaker.run(authorService::findAll,
                t -> {
                    logError(t.getMessage());
                    return Collections.emptyList();
                });
    }
    
    private List<Genre> getAllGenres() {
        return circuitBreaker.run(genreService::findAll,
                t -> {
                    logError(t.getMessage());
                    return Collections.emptyList();
                });
    }
    
    private void logError(String message) {
        log.error(ERROR_TEXT, message);
    }
}
