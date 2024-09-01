package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenreDto {
    private Long id;

    private String name;
}
