package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import(JpaBookRepository.class)
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBooks = getDbBooks();
        for (var expectedBook : expectedBooks) {
            var actualBook = jpaBookRepository.findById(expectedBook.getId());
            assertThat(actualBook).isPresent()
                    .get()
                    .isEqualTo(expectedBook);
        }

    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = jpaBookRepository.findAll();
        var expectedBooks = getDbBooks();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = entityManager.find(Author.class, 1L);
        var genre = entityManager.find(Genre.class, 1L);
        var newBook = new Book(0L, "NewBookTitleTest", author, List.of(genre));

        jpaBookRepository.save(newBook);
        var actualBook = entityManager.find(Book.class, newBook.getId());

        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(newBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var book = entityManager.find(Book.class, 1L);
        book.getGenres().remove(1);

        jpaBookRepository.save(book);
        var actualBook = entityManager.find(Book.class, book.getId());

        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(book);
    }


    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var book = entityManager.find(Book.class, 1L);
        jpaBookRepository.deleteById(book.getId());
        var deletedBook = entityManager.find(Book.class, 1L);
        assertThat(deletedBook).isNull();
    }

    private List<Book> getDbBooks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> entityManager.find(Book.class, id))
                .toList();
    }
}