package ru.otus.hw.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genre/all")
    public String genre(Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genre/genre";
    }
}
