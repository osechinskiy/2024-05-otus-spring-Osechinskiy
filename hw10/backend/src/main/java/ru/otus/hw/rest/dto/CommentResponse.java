package ru.otus.hw.rest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.dto.CommentDto;

@Data
@AllArgsConstructor
public class CommentResponse {

    private long bookId;

    private String bookTitle;

    private String authorName;

    private List<CommentDto> comments;

}
