package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен")
@WebMvcTest(BookController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookMapper bookMapper;

    Author author = new Author(1L, "Author_1");
    List<Genre> genres = List.of(new Genre(1L, "Genre_1"));

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("возвращать все книги")
    @Order(1)
    @Test
    void shouldGetAllBook() throws Exception {
        List<Book> books = List.of(
                new Book(1L, "Book1 Title", author, genres),
                new Book(2L, "Book2 Title", author, genres)
        );

        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/book/all"))
                .andExpect(view().name("book/book"))
                .andExpect(model().attribute("books", books))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создавать новую книгу")
    @Order(2)
    @Test
    void shouldCreateNewBook() throws Exception {
        BookDto bookDto = new BookDto(null, "Title", 1L, Set.of(1L));
        Book book = new Book(1L, "Title", author, genres);

        when(bookService.insert(bookDto)).thenReturn(book);

        mockMvc.perform(post("/book/create").with(csrf()).flashAttr("book", bookDto))
                .andExpect(redirectedUrl("/book/all"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновлять книгу")
    @Order(3)
    @Test
    void shouldUpdateBook() throws Exception {
        BookDto bookDto = new BookDto(1L, "Title", 1L, Set.of(1L));
        Book book = new Book(1L, "Title", author, genres);

        when(bookService.update(bookDto)).thenReturn(book);

        mockMvc.perform(post("/book/edit").with(csrf()).flashAttr("book", bookDto))
                .andExpect(redirectedUrl("/book/all"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("удалять книгу")
    @Order(4)
    @Test
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);
        mockMvc.perform(post("/book/delete/1").with(csrf()))
                .andExpect(redirectedUrl("/book/all"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("провалидировать поля книги")
    @Order(5)
    @Test
    void shouldValidateBookFields() throws Exception {
        BookDto bookDto = new BookDto(null, "T", 1L, Set.of(1L));
        Book book = new Book(1L, "Book Title", author, genres);

        when(bookService.insert(bookDto)).thenReturn(book);
        mockMvc.perform(post("/book/create").with(csrf()).flashAttr("book", bookDto))
                .andExpect(view().name("book/edit"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("выбросить EntityNotFoundException при создании книги")
    @Order(6)
    @Test
    void shouldThrowEntityNotFoundExceptionOnBookCreation() throws Exception {
        BookDto bookDto = new BookDto(null, "New Title Book", 99L, Set.of(1L));

        when(bookService.insert(bookDto)).thenThrow(new EntityNotFoundException("Author is not found"));
        mockMvc.perform(post("/book/create").with(csrf()).flashAttr("book", bookDto))
                .andExpect(view().name("error/entitynotfound"))
                .andExpect(model().attribute("errorMessage", "Author is not found"));
    }

    @DisplayName("возвращать 401 для запроса всех книг")
    @Order(7)
    @Test
    void shouldReturn401AllBooks() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("возвращать 401 для страницы создания новой книги")
    @Order(8)
    @Test
    void shouldReturn401CreateNewBook() throws Exception {
        mockMvc.perform(get("/book/create"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("возвращать 401 для страницы редактирования книги")
    @Order(9)
    @Test
    void shouldReturn401ReturnBook() throws Exception {
        mockMvc.perform(get("/book/edit"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("возвращать 401 для страницы редактирования книги")
    @Order(10)
    @Test
    void shouldReturn401DeleteBook() throws Exception {
        mockMvc.perform(get("/book/delete/1"))
                .andExpect(status().isUnauthorized());
    }
}