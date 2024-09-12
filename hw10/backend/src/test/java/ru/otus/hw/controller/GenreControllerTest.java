package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.controller.GenreController;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер жанров должен")
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @MockBean
    private GenreMapper genreMapper;

    @DisplayName("возвращать все жанры")
    @Test
    void getAllGenres() throws Exception {
        List<Genre> genres = List.of(
                new Genre(1L, "Genre1 Name"),
                new Genre(2L, "Genre2 Name")
        );
        List<GenreDto> genreDtos = genres.stream().map(genreMapper::toDto).toList();

        when(genreService.findAll()).thenReturn(genres);

        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genreDtos)));
    }
}