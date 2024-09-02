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
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер жанров должен")
@WebMvcTest(GenreController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("возвращать все жанры")
    @Order(1)
    @Test
    void getAllGenres() throws Exception {
        List<Genre> genres = List.of(
                new Genre(1L, "Genre1 Name"),
                new Genre(2L, "Genre2 Name")
        );

        when(genreService.findAll()).thenReturn(genres);

        mvc.perform(get("/genre/all").flashAttr("genres", genres))
                .andExpect(status().isOk());
    }

    @DisplayName("возвращать 401 для запроса всех жанров")
    @Order(2)
    @Test
    void shouldReturn401AllBooks() throws Exception {
        mvc.perform(get("/genre/all"))
                .andExpect(status().isUnauthorized());
    }
}