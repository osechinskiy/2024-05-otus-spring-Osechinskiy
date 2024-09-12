package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.rest.controller.AuthorController;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер авторов должен")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorMapper authorMapper;

    @DisplayName("Возвращать всех авторов")
    @Test
    void getAllAuthors() throws Exception {
        List<Author> author = List.of(new Author(1L, "Author_1"));
        List<AuthorDto> authorDto = author.stream().map(authorMapper::toDto).toList();

        when(authorService.findAll()).thenReturn(author);

        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authorDto)));
    }
}