package ru.otus.hw.rest.controller;

import jakarta.validation.Valid;
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
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookValidationDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.rest.dto.BookEditResponse;
import ru.otus.hw.rest.dto.BookRequest;
import ru.otus.hw.rest.dto.ValidationResponse;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    @GetMapping("/api/v1/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll().stream().map(bookMapper::toDto).toList();
    }

    @GetMapping("/api/v1/book")
    public BookEditResponse createBook() {
        List<AuthorDto> authors = authorService.findAll().stream().map(authorMapper::toDto).toList();
        List<GenreDto> genres = genreService.findAll().stream().map(genreMapper::toDto).toList();

        return new BookEditResponse(null, authors, genres);
    }

    @PostMapping("/api/v1/book")
    public ResponseEntity<ValidationResponse> createBook(@RequestBody @Valid BookValidationDto book,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorRespone(bindingResult);
        }
        bookService.insert(book);
        return new ResponseEntity<>(new ValidationResponse(false, null), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/book")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@RequestBody BookRequest bookRequest) {
        bookService.deleteById(bookRequest.getId());
    }

    @GetMapping("/api/v1/book/{id}")
    public BookEditResponse editBook(@PathVariable long id) {
        BookDto book = bookMapper.toDto(bookService.findById(id));
        List<AuthorDto> authors = authorService.findAll().stream().map(authorMapper::toDto).toList();
        List<GenreDto> genres = genreService.findAll().stream().map(genreMapper::toDto).toList();

        return new BookEditResponse(book, authors, genres);
    }

    @PatchMapping("/api/v1/book")
    public ResponseEntity<ValidationResponse> updateBook(@RequestBody @Valid BookValidationDto book,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorRespone(bindingResult);
        }

        bookService.update(book);
        return new ResponseEntity<>(new ValidationResponse(false, null), HttpStatus.ACCEPTED);
    }

    private ResponseEntity<ValidationResponse> errorRespone(BindingResult bindingResult) {
        ValidationResponse response = new ValidationResponse();
        Map<String, String> errorMap = bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                        (oldValue, newValue) -> newValue));
        response.setHasError(true);
        response.setBadFields(errorMap);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
