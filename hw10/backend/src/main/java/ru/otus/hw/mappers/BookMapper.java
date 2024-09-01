package ru.otus.hw.mappers;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final GenreMapper genreMapper;

    private final AuthorMapper authorMapper;

    public BookDto toDto(Book book) {
        Set<GenreDto> genreDto = book.getGenres().stream().map(genreMapper::toDto).collect(Collectors.toSet());
        return new BookDto(book.getId(), book.getTitle(), authorMapper.toDto(book.getAuthor()), genreDto);
    }
}
