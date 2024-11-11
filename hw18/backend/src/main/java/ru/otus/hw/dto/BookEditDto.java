package ru.otus.hw.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEditDto {

    private String id;

    private String title;

    private AuthorDto author;

    private Set<GenreDto> genres;

}
