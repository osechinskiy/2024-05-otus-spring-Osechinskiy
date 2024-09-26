package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.mongo.MongoGenre;

@Component
public class GenreProcessor implements ItemProcessor<MongoGenre, GenreDto> {

    @Override
    public GenreDto process(MongoGenre item) {
        return new GenreDto(item.getId(), item.getName());
    }
}
