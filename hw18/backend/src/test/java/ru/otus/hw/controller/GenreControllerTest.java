package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import ru.otus.hw.rest.controller.GenreController;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Контроллер жанров должен ")
@ExtendWith(SpringExtension.class)
@Import(GenreMapper.class)
@WebFluxTest(controllers = GenreController.class)
class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreMapper mapper;

    @DisplayName("возвращать все жанры")
    @Test
    void shouldReturnAllGenres() throws Exception {
        var genres = List.of(
                new Genre("1", "Genre1 Name"),
                new Genre("2", "Genre2 Name")
        );
        BDDMockito.given(genreRepository.findAll()).willReturn(Flux.fromIterable(genres));

        var result = webTestClient.get().uri("/api/v1/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(GenreDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<GenreDto> stepResult = null;
        for (var genre : genres) {
            stepResult = step.expectNext(mapper.toDto(genre));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }
}
