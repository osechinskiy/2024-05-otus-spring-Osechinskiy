package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorMapper {

    public Author toEntity(AuthorDto dto) {
        return new Author(dto.getId(), dto.getFullName());
    }

    public AuthorDto toDto(Author entity) {
        return new AuthorDto(entity.getId(), entity.getFullName());
    }
}
