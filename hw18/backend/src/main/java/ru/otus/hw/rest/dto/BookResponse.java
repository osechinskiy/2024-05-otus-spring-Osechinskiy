package ru.otus.hw.rest.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;

@Data
@AllArgsConstructor
public class BookResponse {

    private long id;

    private String title;

    private AuthorDto author;

    private Set<GenreDto> genres;

}
