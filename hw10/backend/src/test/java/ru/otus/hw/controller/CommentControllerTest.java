package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.controller.CommentController;
import ru.otus.hw.rest.dto.CommentResponse;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер комментариев должен")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentMapper commentMapper;

    @DisplayName("Возвращать все комментарии к книге")
    @Test
    void getAllCommentByBook() throws Exception {
        Author author = new Author(1L, "Author1 Name");
        Genre genre = new Genre(1L, "Genre1 Name");
        Book book = new Book(1L, "Book1 Name", author, List.of(genre));
        List<Comment> comments = List.of(new Comment(1L, book, "Comment1 Text"));
        CommentResponse commentResponse = new CommentResponse(book.getId(), book.getTitle(),
                book.getAuthor().getFullName(), comments.stream().map(commentMapper::toDto).toList());

        when(bookService.findById(1L)).thenReturn(book);
        when(commentService.findByBookId(1L)).thenReturn(comments);

        mvc.perform(get("/api/v1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentResponse)));
    }
}