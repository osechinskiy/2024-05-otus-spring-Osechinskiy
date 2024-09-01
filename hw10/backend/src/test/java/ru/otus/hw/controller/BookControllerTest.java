package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookValidationDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.controller.BookController;
import ru.otus.hw.rest.dto.BookEditResponse;
import ru.otus.hw.rest.dto.BookRequest;
import ru.otus.hw.rest.dto.ValidationResponse;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private AuthorMapper authorMapper;

    @MockBean
    private GenreMapper genreMapper;

    Author author = new Author(1L, "Author_1");
    List<Genre> genres = List.of(new Genre(1L, "Genre_1"));

    @DisplayName("возвращать все книги")
    @Test
    void shouldGetAllBook() throws Exception {
        List<Book> books = List.of(
                new Book(1L, "Book1 Title", author, genres),
                new Book(2L, "Book2 Title", author, genres)
        );
        List<BookDto> bookDtos = books.stream().map(bookMapper::toDto).toList();

        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDtos)));
    }

    @DisplayName("возвращать книгу по id")
    @Test
    void shouldReturnBookById() throws Exception {
        Book book = new Book(1L, "Book1 Title", author, genres);

        when(bookService.findById(1L)).thenReturn(book);
        when(authorService.findAll()).thenReturn(List.of(author));
        when(genreService.findAll()).thenReturn(genres);
        when(authorMapper.toDto(author)).thenCallRealMethod();

        List<AuthorDto> authorDtos = List.of(authorMapper.toDto(author));
        List<GenreDto> genreDtos = genres.stream().map(genreMapper::toDto).toList();
        BookEditResponse bookEditResponse = new BookEditResponse(bookMapper.toDto(book), authorDtos, genreDtos);

        mockMvc.perform(get("/api/v1/book/%d".formatted(book.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookEditResponse)));
    }

    @DisplayName("возвращать 404 при поиске несуществующей книги")
    @Test
    void shouldReturnNotFoundOnFindById() throws Exception {
        when(bookService.findById(1L)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/api/books/%d".formatted(1L)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("создавать новую книгу")
    @Test
    void shouldCreateNewBook() throws Exception {
        BookValidationDto bookDto = new BookValidationDto(null, "Title", 1L, Set.of(1L));
        Book book = new Book(1L, "Title", author, genres);

        when(bookService.insert(bookDto)).thenReturn(book);

        var content = post("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));

        mockMvc.perform(content)
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(new ValidationResponse(false, null))));
    }

    @DisplayName("обновлять книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        BookValidationDto bookDto = new BookValidationDto(1L, "Title", 1L, Set.of(1L));
        Book book = new Book(1L, "Title", author, genres);

        when(bookService.update(bookDto)).thenReturn(book);

        var content = patch("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));

        mockMvc.perform(content)
                .andExpect(status().isAccepted())
                .andExpect(content().json(mapper.writeValueAsString(new ValidationResponse(false, null))));
    }

    @DisplayName("удалять книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);

        var content = delete("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new BookRequest(1L)));

        mockMvc.perform(content)
                .andExpect(status().isNoContent());
    }

    @DisplayName("провалидировать поля книги")
    @Test
    void shouldValidateBookFields() throws Exception {
        BookValidationDto bookDto = new BookValidationDto(null, "T", 1L, Set.of(1L));
        Book book = new Book(1L, "Book Title", author, genres);

        when(bookService.insert(bookDto)).thenReturn(book);

        var content = post("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));

        mockMvc.perform(content)
                .andExpect(status().isBadRequest());
    }

    @DisplayName("выбросить EntityNotFoundException при создании книги")
    @Test
    void shouldThrowEntityNotFoundExceptionOnBookCreation() throws Exception {
        BookValidationDto bookDto = new BookValidationDto(null, "New Title Book", 99L, Set.of(1L));

        when(bookService.insert(bookDto)).thenThrow(new EntityNotFoundException("Author is not found"));

        var content = post("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));

        mockMvc.perform(content)
                .andExpect(status().isNotFound());
    }
}