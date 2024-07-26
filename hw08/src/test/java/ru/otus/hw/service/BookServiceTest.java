package ru.otus.hw.service;

import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис для работы с книгами ")
@DataMongoTest
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceTest {

    @Autowired
    BookService bookService;

    @DisplayName("должен найти книгу по id")
    @Test
    @Order(1)
    void shouldFindBookById() {
        var actualBook = bookService.findById("1");

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getId()).isEqualTo("1");
        assertThat(actualBook.get().getTitle()).isEqualTo("BookTitle_1");
        assertThat(actualBook.get().getAuthor()).isNotNull();
        assertThat(actualBook.get().getAuthor().getId()).isEqualTo("1");
        assertThat(actualBook.get().getGenres()).isNotEmpty().hasSize(2);
    }

    @DisplayName("должен найти все книги")
    @Test
    @Order(2)
    void shouldFindAllBooks() {
        var actualBooks = bookService.findAll();

        assertThat(actualBooks)
                .isNotEmpty()
                .hasSize(3)
                .allMatch(b -> b.getTitle() != null)
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenres() != null && !b.getGenres().isEmpty());
    }

    @DisplayName("должен добавлять новую книгу")
    @Test
    @Order(3)
    void shouldAddNewBook() {
        var insertedBook = bookService.insert("Test_Title_book", "1", Set.of("1"));
        var dbBook = bookService.findById(insertedBook.getId());

        assertThat(insertedBook)
                .usingRecursiveComparison()
                .isEqualTo(dbBook.get());
    }

    @DisplayName("должен выбросить EntityNotFoundException при создании")
    @Test
    @Order(4)
    void shouldThrowEntityNotFoundExceptionAtInsert() {
        assertThrows(EntityNotFoundException.class, () -> bookService.insert("Test_Title_book", "1111", Set.of("1")));
    }

    @DisplayName("должен обновлять книгу")
    @Test
    @Order(5)
    void shouldUpdateBook() {
        var oldBook = bookService.findById("1");
        var genreIds = oldBook.get().getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        var updatedBook = bookService.update(oldBook.get().getId(), "New title", oldBook.get().getAuthor().getId(), genreIds);
        var dbBook = bookService.findById("1");

        assertThat(updatedBook)
                .usingRecursiveComparison()
                .isEqualTo(dbBook.get());
    }

    @DisplayName("должен выбросить EntityNotFoundException при обновлении")
    @Test
    @Order(6)
    void shouldThrowEntityNotFoundExceptionAtUpdate() {
        var oldBook = bookService.findById("1");
        var genreIds = oldBook.get().getGenres().stream().map(Genre::getId).collect(Collectors.toSet());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.update(oldBook.get().getId(), "New title", "0", genreIds));
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    @Order(7)
    void shouldDeleteBookById() {
        bookService.deleteById("1");
        var actualBook = bookService.findById("1");

        assertThat(actualBook).isEmpty();
    }
}
