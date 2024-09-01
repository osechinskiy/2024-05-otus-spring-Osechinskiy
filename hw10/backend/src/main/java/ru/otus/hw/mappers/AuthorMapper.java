package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.rest.dto.AuthorResponse;

@Component
public class AuthorMapper {
    public AuthorResponse toResponse(Author entity) {
        return new AuthorResponse(entity.getId(), entity.getFullName());
    }

    public AuthorDto toDto(Author entity) {
        return new AuthorDto(entity.getId(), entity.getFullName());
    }
}
