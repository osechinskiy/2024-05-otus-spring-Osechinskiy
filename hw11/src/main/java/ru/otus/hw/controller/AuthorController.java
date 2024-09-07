package ru.otus.hw.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.AuthorService;

@Controller
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author/all")
    public String getAllAuthors(Model model) {
        var authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "author/author";
    }
}
