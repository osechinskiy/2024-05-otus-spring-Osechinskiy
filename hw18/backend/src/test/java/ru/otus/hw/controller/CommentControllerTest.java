package ru.otus.hw.controller;

import java.util.List;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.rest.controller.CommentController;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Контроллер комментариев должен ")
@ExtendWith(SpringExtension.class)
@Import({AuthorMapper.class, BookMapper.class, CommentMapper.class, GenreMapper.class})
@WebFluxTest(controllers = CommentController.class)
class CommentControllerTest {

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CommentMapper mapper;

    @DisplayName("возвращать все коммантарии к книге")
    @Test
    void shouldReturnCommentsByBookId() throws Exception {
        var author = new Author("1", "Author1 Name");
        var genre = new Genre("1", "Genre1 Name");
        var book = new Book("1", "Book1 Name", author, List.of(genre));
        var comments = List.of(new Comment("1", book, "Comment1 Text"));
        BDDMockito.given(bookRepository.findById("1")).willReturn(Mono.just(book));
        BDDMockito.given(commentRepository.findByBookId("1")).willReturn(Flux.fromIterable(comments));

        var result = webTestClient.get()
                .uri("/api/v1/comment/%s".formatted(book.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CommentDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<CommentDto> stepResult = null;
        for (var comment : comments) {
            stepResult = step.expectNext(mapper.toDto(comment));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }
}
