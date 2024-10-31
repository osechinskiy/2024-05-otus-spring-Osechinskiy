package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер жанров должен")
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CircuitBreaker circuitBreaker;

    @DisplayName("возвращать все жанры")
    @Test
    void getAllGenres() throws Exception {
        List<Genre> genres = List.of(
                new Genre(1L, "Genre1 Name"),
                new Genre(2L, "Genre2 Name")
        );

        when(circuitBreaker.run(any(), any())).thenReturn(genres);

        mvc.perform(get("/genre/all").flashAttr("genres", genres))
                .andExpect(status().isOk());
    }
}