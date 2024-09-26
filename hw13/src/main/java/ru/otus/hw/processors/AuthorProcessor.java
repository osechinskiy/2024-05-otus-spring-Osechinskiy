package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.mongo.MongoAuthor;

@Component
public class AuthorProcessor implements ItemProcessor<MongoAuthor, AuthorDto> {

    @Override
    public AuthorDto process(MongoAuthor item) throws Exception {
        return new AuthorDto(item.getId(), item.getFullName());
    }
}
