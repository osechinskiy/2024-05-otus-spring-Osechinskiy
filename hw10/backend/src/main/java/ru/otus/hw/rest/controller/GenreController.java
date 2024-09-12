package ru.otus.hw.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.rest.dto.GenreResponse;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    private final GenreMapper genreMapper;

    @GetMapping("/api/v1/genres")
    public List<GenreResponse> genre() {
        return genreService.findAll().stream().map(genreMapper::toResponse).toList();
    }
}
