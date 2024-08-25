package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDto toDto(Book book) {
        var genreIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId(), genreIds);
    }
}
