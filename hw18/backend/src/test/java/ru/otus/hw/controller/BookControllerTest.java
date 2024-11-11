package ru.otus.hw.controller;

import java.util.stream.Collectors;
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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookValidationDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import ru.otus.hw.rest.controller.BookController;
import ru.otus.hw.rest.dto.ValidationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;

@DisplayName("Контроллер книг должен ")
@ExtendWith(SpringExtension.class)
@Import({AuthorMapper.class, BookMapper.class, CommentMapper.class, GenreMapper.class})
@WebFluxTest(controllers = BookController.class)
class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookMapper mapper;

    @DisplayName("возвращать все книги")
    @Test
    void shouldReturnAllBooks() {
        var author = new Author("1", "Author2 Name");
        var genres = List.of(new Genre("1", "Genre1 Name"));
        var books = List.of(
                new Book("1", "Book1 Title", author, genres),
                new Book("2", "Book2 Title", author, genres)
        );
        BDDMockito.given(bookRepository.findAll()).willReturn(Flux.fromIterable(books));

        var result = webTestClient.get().uri("/api/v1//books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = null;
        for (Book book : books) {
            stepResult = step.expectNext(mapper.toDto(book));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("возвращать книгу по id")
    @Test
    void shouldReturnBookById() {
        var author = new Author("1", "Author2 Name");
        var genres = List.of(new Genre("1", "Genre1 Name"));
        var book = new Book("1", "Book1 Title", author, genres);
        BDDMockito.given(bookRepository.findById("1")).willReturn(Mono.just(book));

        var result = webTestClient.get().uri("/api/v1/books/%s".formatted(book.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(mapper.toDto(book));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("возвращать 404 при поиске несуществующей книги")
    @Test
    void shouldReturnNotFoundOnFindById() {
        BDDMockito.given(bookRepository.findById("1")).willThrow(EntityNotFoundException.class);

        webTestClient.get()
                .uri("/api/v1/books/".concat("1"))
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("создавать новую книгу")
    @Test
    void shouldCreateNewBook() {
        var author = new Author("1", "Author2 Name");
        var genres = List.of(new Genre("1", "Genre1 Name"));
        var newBook = new Book(null, "New Book Title", author, genres);
        var createdBook = new Book("1", "New Book Title", author, genres);
        var validation = new BookValidationDto("1", "New Book Title", author.getId(),
                genres.stream().map(Genre::getId).collect(
                        Collectors.toSet()));
        BDDMockito.given(authorRepository.findById("1")).willReturn(Mono.just(author));
        BDDMockito.given(bookRepository.save(newBook)).willReturn(Mono.just(createdBook));
        BDDMockito.given(genreRepository.findAllById(anyIterable())).willReturn(Flux.fromIterable(genres));

        var result = webTestClient
                .post().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validation)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(ValidationResponse.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<ValidationResponse> stepResult = step.expectNext(new ValidationResponse(false, null));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("обновлять книгу")
    @Test
    void shouldUpdateBook() {
        var author = new Author("1", "Author2 Name");
        var genres = List.of(new Genre("1", "Genre1 Name"));
        var book = new Book("1", "New Book Title", author, genres);
        var validation = new BookValidationDto("1", "New Book Title", author.getId(),
                genres.stream().map(Genre::getId).collect(
                        Collectors.toSet()));
        BDDMockito.given(authorRepository.findById("1")).willReturn(Mono.just(author));
        BDDMockito.given(bookRepository.save(book)).willReturn(Mono.just(book));
        BDDMockito.given(genreRepository.findAllById(anyIterable())).willReturn(Flux.fromIterable(genres));

        var result = webTestClient
                .patch().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validation)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(ValidationResponse.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<ValidationResponse> stepResult = step.expectNext(new ValidationResponse(false, null));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("удалять книгу")
    @Test
    void shouldDeleteBook() {
        BDDMockito.given(bookRepository.deleteById((String) any())).willReturn(Mono.empty());

        var result = webTestClient
                .delete().uri("/api/v1/books/1")
                .exchange()
                .expectStatus().isNoContent()
                .returnResult(Void.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        step.verifyComplete();
    }

    @DisplayName("провалидировать поля книги")
    @Test
    void shouldValidateBookFields() {
        var author = new Author("1", "Author2 Name");
        var genres = List.of(new Genre("1", "Genre1 Name"));
        var book = new Book(null, "N", author, genres);//wrong title
        BDDMockito.given(bookRepository.save(book)).willReturn(Mono.just(book));

        webTestClient
                .post().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
