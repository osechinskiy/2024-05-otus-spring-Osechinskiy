package ru.otus.hw.rest.controller;


import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.services.AuthorService;

@RestController
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorMapper authorMapper;

    @GetMapping("/api/v1/authors")
    public List<AuthorDto> getAllAuthors() {
        return authorService.findAll().stream().map(authorMapper::toDto).toList();
    }
}
