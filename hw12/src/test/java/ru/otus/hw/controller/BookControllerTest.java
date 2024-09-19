package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;


    Author author = new Author(1L, "Author_1");
    List<Genre> genres = List.of(new Genre(1L, "Genre_1"));

    @WithMockUser(
            username = "user1",
            authorities = {"ROLE_USER1"}
    )
    @DisplayName("возвращать только первую книгу")
    @Order(1)
    @Test
    void shouldReturnExpectedBooksForFirstUser() throws Exception {
        Author author1 = new Author(1L, "Author_1");
        List<Genre> genres1 = List.of(
                new Genre(1L, "Genre_1"),
                new Genre(2L, "Genre_2")
        );
        List<Book> books = List.of(
                new Book(1L, "BookTitle_1", author1, genres1)
        );

        mockMvc.perform(get("/book/all"))
                .andExpect(view().name("book/book"))
                .andExpect(model().attribute("books", books))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user2",
            authorities = {"ROLE_USER2"}
    )
    @DisplayName("возвращать только вторую книгу")
    @Order(2)
    @Test
    void shouldReturnExpectedBooksForSecondUser() throws Exception {
        Author author2 = new Author(2L, "Author_2");
        List<Genre> genres2 = List.of(
                new Genre(3L, "Genre_3"),
                new Genre(4L, "Genre_4")
        );
        List<Book> books = List.of(
                new Book(2L, "BookTitle_2", author2, genres2)
        );

        mockMvc.perform(get("/book/all"))
                .andExpect(view().name("book/book"))
                .andExpect(model().attribute("books", books))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user1",
            authorities = {"ROLE_USER1"}
    )
    @Test
    @Order(3)
    void shouldReturn403ForUpdatedBook() throws Exception {
        mockMvc.perform(post("/book/edit"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("успешно обновить книгу")
    @Test
    @Order(4)
    void shouldUpdateFirstBook() throws Exception {
        BookDto bookDto = new BookDto(1L, "New_Title", 1L, Set.of(1L, 2L));
        mockMvc.perform(post("/book/edit").with(csrf()).flashAttr("book", bookDto))
                .andExpect(redirectedUrl("/book/all"));
    }

    @WithMockUser(
            username = "user1",
            authorities = {"ROLE_USER1"}
    )
    @DisplayName("получить 403 для удаления книги")
    @Test
    @Order(5)
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(post("/book/delete/1").with(csrf()))
                .andExpect(status().isForbidden());
    }
}