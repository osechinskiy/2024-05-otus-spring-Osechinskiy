package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.MongoGenre;

public interface MongoGenreRepository extends MongoRepository<MongoGenre, String> {

}
