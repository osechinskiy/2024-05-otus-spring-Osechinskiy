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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер комментариев должен")
@WebMvcTest(CommentController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("Возвращать все комментарии к книге")
    @Order(1)
    @Test
    void getAllCommentByBook() throws Exception {
        Author author = new Author(1L, "Author1 Name");
        Genre genre = new Genre(1L, "Genre1 Name");
        Book book = new Book(1L, "Book1 Name", author, List.of(genre));
        List<Comment> comments = List.of(new Comment(1L, book,"Comment1 Text"));

        when(bookService.findById(1l)).thenReturn(book);
        when(commentService.findByBookId(1l)).thenReturn(comments);

        mvc.perform(get("/comment/1").flashAttr("comments", comments))
                .andExpect(status().isOk());
    }

    @DisplayName("возвращать 401 для запроса комментариев к книге")
    @Order(2)
    @Test
    void shouldReturn401AllBooks() throws Exception {
        mvc.perform(get("/commen/1"))
                .andExpect(status().isUnauthorized());
    }
}