package ru.otus.hw.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().map(authorMapper::toDto);
    }
}
