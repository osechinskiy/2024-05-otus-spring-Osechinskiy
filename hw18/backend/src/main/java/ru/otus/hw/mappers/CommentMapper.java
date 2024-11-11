package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
