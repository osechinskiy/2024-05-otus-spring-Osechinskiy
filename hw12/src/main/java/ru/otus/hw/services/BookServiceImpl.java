package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclServiceWrapperService;

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public Book findById(long id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Book insert(BookDto book) {
        Book newBook = save(0, book.getTitle(), book.getAuthorId(), book.getGenres());
        aclServiceWrapperService.createPermission(newBook);
        return newBook;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#bookDto, 'WRITE')")
    public Book update(@Param("bookDto") BookDto book) {
        return save(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenres());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (CollectionUtils.isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (CollectionUtils.isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
