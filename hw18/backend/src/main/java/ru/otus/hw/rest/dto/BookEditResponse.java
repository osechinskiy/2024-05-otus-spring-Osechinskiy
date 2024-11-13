package ru.otus.hw.rest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEditResponse {

    private BookDto book;

    private List<AuthorDto> authors;

    private List<GenreDto> genres;
}
