package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final BookMapper bookMapper;

    public Comment toEntity(CommentDto dto) {
        return new Comment(dto.getId(), null, dto.getText());
    }

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
