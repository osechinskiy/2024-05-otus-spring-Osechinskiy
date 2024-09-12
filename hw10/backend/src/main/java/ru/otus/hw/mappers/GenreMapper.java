package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.dto.GenreResponse;

@Component
public class GenreMapper {
    public GenreResponse toResponse(Genre genre) {
        return new GenreResponse(genre.getId(), genre.getName());
    }

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
