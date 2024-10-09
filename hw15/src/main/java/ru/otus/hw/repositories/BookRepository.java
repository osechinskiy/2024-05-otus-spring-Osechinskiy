package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "book")
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph("book-graph")
    List<Book> findAll();

    @Override
    @EntityGraph("book-graph")
    @RestResource(path = "bookId", rel = "bookId")
    Optional<Book> findById(Long bookId);
}
