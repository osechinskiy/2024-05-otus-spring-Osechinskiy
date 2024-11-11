package ru.otus.hw.controller;

import java.util.List;
import org.assertj.core.api.Assertions;
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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.rest.controller.AuthorController;
import reactor.test.StepVerifier;


@DisplayName("Контроллер авторов должен ")
@ExtendWith(SpringExtension.class)
@Import(AuthorMapper.class)
@WebFluxTest(controllers = AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper mapper;

    @DisplayName("возвращать всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        var authors = List.of(
                new Author("1", "Author1 Name"),
                new Author("2", "Author2 Name")
        );
        BDDMockito.given(authorRepository.findAll()).willReturn(Flux.fromIterable(authors));

        var result = webTestClient.get().uri("/api/v1/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AuthorDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<AuthorDto> stepResult = null;
        for (var author : authors) {
            stepResult = step.expectNext(mapper.toDto(author));
        }

        Assertions.assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

}
