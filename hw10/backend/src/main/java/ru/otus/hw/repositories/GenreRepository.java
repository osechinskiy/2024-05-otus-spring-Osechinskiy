package ru.otus.hw.repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAllByIdIn(Collection<Long> ids);
}
