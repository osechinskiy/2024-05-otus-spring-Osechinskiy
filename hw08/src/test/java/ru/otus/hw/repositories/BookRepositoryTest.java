package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать книгу по id")
    @Test
    @Order(1)
    void shouldReturnCorrectBookById() {
        var expectedBooks = getDbBooks();
        for (var expectedBook : expectedBooks) {
            var actualBook = bookRepository.findById(expectedBook.getId());
            assertThat(actualBook).isPresent()
                    .get()
                    .isEqualTo(expectedBook);
        }

    }

    @DisplayName("должен загружать список всех книг")
    @Test
    @Order(2)
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = getDbBooks();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    @Order(3)
    void shouldSaveNewBook() {
        var author = mongoTemplate.findById("1", Author.class);
        var genre = mongoTemplate.findById("1", Genre.class);
        var newBook = new Book(null, "NewBookTitleTest", author, List.of(genre));

        bookRepository.save(newBook);
        var actualBook = mongoTemplate.findById(newBook.getId(), Book.class);

        assertThat(actualBook).isEqualTo(newBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @Order(4)
    void shouldSaveUpdatedBook() {
        var book = mongoTemplate.findById("1", Book.class);
        book.getGenres().remove(1);

        bookRepository.save(book);
        var actualBook = mongoTemplate.findById(book.getId(), Book.class);

        assertThat(actualBook).isEqualTo(book);
    }


    @DisplayName("должен удалять книгу по id ")
    @Test
    @Order(5)
    void shouldDeleteBook() {
        var book = mongoTemplate.findById("1", Book.class);
        bookRepository.deleteById(book.getId());
        var deletedBook = mongoTemplate.findById("1", Book.class);
        assertThat(deletedBook).isNull();
    }

    private List<Book> getDbBooks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> mongoTemplate.findById(String.valueOf(id), Book.class))
                .toList();
    }
}