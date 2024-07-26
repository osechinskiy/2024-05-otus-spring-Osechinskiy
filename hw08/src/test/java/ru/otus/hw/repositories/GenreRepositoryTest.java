package ru.otus.hw.repositories;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать жанры по id")
    @Test
    @Order(1)
    void shouldReturnCorrectGenresByIds() {
        List<Genre> expectedGenres = getDbGenres();
        var expectedGenreIds = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        var actualGenres = genreRepository.findAllByIdIn(expectedGenreIds);
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    @Order(2)
    void shouldReturnCorrectAuthorsList() {
        var actualGenres = genreRepository.findAll();
        var expectedGenres = getDbGenres();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    private List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> mongoTemplate.findById(String.valueOf(id), Genre.class))
                .toList();
    }
}
