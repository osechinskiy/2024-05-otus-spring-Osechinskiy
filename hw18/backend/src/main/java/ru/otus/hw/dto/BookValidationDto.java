package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookValidationDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    @NotBlank(message = "Title can not be empty")
    @Size(min = 2, max = 100, message = "Title should be between 2 and 100 characters")
    private String title;

    @JsonProperty("authorId")
    @NotNull(message = "Author can not be empty")
    private String authorId;

    @JsonProperty("genres")
    @NotEmpty(message = "Book must contain at least one genre")
    private Set<String> genres;

}
