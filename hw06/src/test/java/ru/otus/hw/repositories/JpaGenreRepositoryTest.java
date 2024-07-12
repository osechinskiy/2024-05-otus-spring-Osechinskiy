package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("должен загружать жанры по id")
    @Test
    void shouldReturnCorrectGenresByIds() {
        List<Genre> expectedGenres = getDbGenres();
        var expectedGenreIds = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        var actualGenres = jpaGenreRepository.findAllByIds(expectedGenreIds);
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualGenres = jpaGenreRepository.findAll();
        var expectedGenres = getDbGenres();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    private List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> entityManager.find(Genre.class, id))
                .toList();
    }
}
