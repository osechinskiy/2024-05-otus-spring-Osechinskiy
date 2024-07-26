package ru.otus.hw.repositories;

import java.util.Collection;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, Long> {
    List<Genre> findAllByIdIn(Collection<String> ids);
}
