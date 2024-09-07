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
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookValidationDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.rest.dto.BookRequest;
import ru.otus.hw.rest.dto.ValidationResponse;
import ru.otus.hw.services.BookService;
import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping("/api/v1/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll().stream().map(bookMapper::toDto).toList();
    }

    @PostMapping("/api/v1/books")
    public ResponseEntity<ValidationResponse> createBook(@RequestBody @Valid BookValidationDto book,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorRespone(bindingResult);
        }
        bookService.insert(book);
        return new ResponseEntity<>(new ValidationResponse(false, null), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@RequestBody BookRequest bookRequest) {
        bookService.deleteById(bookRequest.getId());
    }

    @GetMapping("/api/v1/books/{id}")
    public BookDto getBook(@PathVariable long id) {
        return bookMapper.toDto(bookService.findById(id));
    }

    @PatchMapping("/api/v1/books")
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
