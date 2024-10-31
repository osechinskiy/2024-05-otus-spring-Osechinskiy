package ru.otus.hw.controller;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

@Controller
@Log4j2
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final CircuitBreaker circuitBreaker;

    @GetMapping("/author/all")
    public String getAllAuthors(Model model) {
        List<Author> authors = circuitBreaker.run(authorService::findAll,
                t -> {
                    log.error("delay call failed error:{}", t.getMessage());
                    return Collections.emptyList();
                });

        model.addAttribute("authors", authors);
        return "author/author";
    }
}
