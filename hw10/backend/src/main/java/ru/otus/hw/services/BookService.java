package ru.otus.hw.services;

import ru.otus.hw.models.dto.BookValidationDto;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {
    Book findById(long id);

    List<Book> findAll();

    Book insert(BookValidationDto book);

    Book update(BookValidationDto book);

    void deleteById(long id);
}
