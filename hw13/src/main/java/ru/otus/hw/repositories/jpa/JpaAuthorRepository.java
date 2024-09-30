package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.jpa.JpaAuthor;

public interface JpaAuthorRepository extends JpaRepository<JpaAuthor, Long> {

}
