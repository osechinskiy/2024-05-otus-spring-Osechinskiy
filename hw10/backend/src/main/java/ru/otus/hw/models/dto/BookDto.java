package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private long id;

    private String title;

    private AuthorDto author;

    private Set<GenreDto> genres;
}
