package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.mongo.MongoBook;

@Component
public class BookProcessor implements ItemProcessor<MongoBook, BookDto> {

    @Override
    public BookDto process(MongoBook item) {
        return new BookDto(item.getId(), item.getTitle(), item.getAuthor().getId(), item.getGenre().getId());
    }
}
