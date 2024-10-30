package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BookDto {
    private Long id;

    @NotBlank(message = "Title can not be empty")
    @Size(min = 2, max = 100, message = "Title should be between 2 and 100 characters")
    private String title;

    @NotNull(message = "Author can not be empty")
    private Long authorId;

    @NotNull(message = "Book must contain at least one genre")
    private Set<Long> genres;
}
