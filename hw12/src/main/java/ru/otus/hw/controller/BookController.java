package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookMapper bookMapper;

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/book/all";
    }

    @GetMapping("/book/all")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/book";
    }

    @GetMapping("/book/create")
    public String createBook(Model model) {
        BookDto book = new BookDto(null, null, null, null);
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book/edit";
    }

    @PostMapping("/book/create")
    public String createBook(@Valid @ModelAttribute("book") BookDto book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
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
        Book book = bookService.findById(id);
        model.addAttribute("book", bookMapper.toDto(book));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
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
}
