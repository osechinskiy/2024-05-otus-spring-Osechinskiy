package ru.otus.hw.rest.controller;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookValidationDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.ValidationResponse;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> getAllBooks() {
        return bookRepository.findAll().map(bookMapper::toDto);
    }

    @PostMapping("/api/v1/books")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<ResponseEntity<ValidationResponse>> createBook(@RequestBody @Valid BookValidationDto book) {
        return save(null, book.getTitle(), book.getAuthorId(), book.getGenres())
                .map(b -> new ResponseEntity<>(new ValidationResponse(false, null), HttpStatus.CREATED))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id)
                .then(Mono.fromCallable(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBook(@PathVariable(required = false) String id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PatchMapping("/api/v1/books")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<ValidationResponse>> updateBook(@RequestBody @Valid BookValidationDto book) {
        return save(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenres())
                .map(b -> new ResponseEntity<>(new ValidationResponse(false, null), HttpStatus.CREATED))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    private Mono<ResponseEntity<ValidationResponse>> errorResponse(BindingResult bindingResult) {
        ValidationResponse response = new ValidationResponse();
        Map<String, String> errorMap = bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                        (oldValue, newValue) -> newValue));
        response.setHasError(true);
        response.setBadFields(errorMap);
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }

    private Mono<Book> save(String id, String title, String authorId, Collection<String> genresIds) {
        var genresFlux = genreRepository.findAllById(genresIds).collectList();
        var authorMono = authorRepository.findById(authorId);

        return Mono.zip(genresFlux, authorMono, (genres, author) -> new Book(id, title, author, genres))
                .flatMap(bookRepository::save);
    }
}
