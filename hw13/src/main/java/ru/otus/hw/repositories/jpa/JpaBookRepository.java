package ru.otus.hw.repositories.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.jpa.JpaBook;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {

    @Override
    @EntityGraph("book-graph")
    List<JpaBook> findAll();

}
