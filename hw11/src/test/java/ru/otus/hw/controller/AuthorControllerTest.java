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
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер авторов должен")
@WebMvcTest(AuthorController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("Возвращать всех авторов")
    @Order(1)
    @Test
    void getAllAuthors() throws Exception {
        List<Author> author = List.of(new Author(1L, "Author_1"));

        when(authorService.findAll()).thenReturn(author);

        mvc.perform(get("/author/all").flashAttr("authors", author))
                .andExpect(status().isOk());
    }

    @DisplayName("возвращать 401 для запроса всех авторов")
    @Order(2)
    @Test
    void shouldReturn401AllBooks() throws Exception {
        mvc.perform(get("/author/all"))
                .andExpect(status().isUnauthorized());
    }
}