package ru.otus.hw.repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.models.Genre;

import java.util.List;

@RepositoryRestResource(path = "genre")
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @RestResource(path = "genreIds", rel = "genreIds")
    List<Genre> findAllByIdIn(Collection<Long> ids);
}
