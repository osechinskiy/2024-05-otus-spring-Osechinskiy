package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.mongo.MongoComment;

@Component
public class CommentProcessor implements ItemProcessor<MongoComment, CommentDto> {

    @Override
    public CommentDto process(MongoComment item) {
        return new CommentDto(item.getId(), item.getText(), item.getBook().getId());
    }
}
