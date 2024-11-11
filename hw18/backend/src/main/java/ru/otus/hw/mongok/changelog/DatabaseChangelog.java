package ru.otus.hw.mongok.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog
public class DatabaseChangelog {
    private Author author1;

    private Author author2;

    private Author author3;

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    private Genre genre4;

    private Genre genre5;

    private Genre genre6;

    private Book book1;

    private Book book2;

    private Book book3;
    
    @ChangeSet(order = "001", id = "dropDb", author = "boris.osechinskiy", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "boris.osechinskiy")
    public void insertAuthors(AuthorRepository authorRepository) {
        author1 = authorRepository.save(new Author("1", "Author_1")).block();
        author2 = authorRepository.save(new Author("2", "Author_2")).block();
        author3 = authorRepository.save(new Author("3", "Author_3")).block();
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "boris.osechinskiy")
    public void insertGenres(GenreRepository genreRepository) {
        genre1 = genreRepository.save(new Genre("1", "Genre_1")).block();
        genre2 = genreRepository.save(new Genre("2", "Genre_2")).block();
        genre3 = genreRepository.save(new Genre("3", "Genre_3")).block();
        genre4 = genreRepository.save(new Genre("4", "Genre_4")).block();
        genre5 = genreRepository.save(new Genre("5", "Genre_5")).block();
        genre6 = genreRepository.save(new Genre("6", "Genre_6")).block();
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "boris.osechinskiy")
    public void insertBooks(BookRepository bookRepository) {
        book1 = bookRepository.save(new Book("1", "BookTitle_1", author1, List.of(genre1, genre2))).block();
        book2 = bookRepository.save(new Book("2", "BookTitle_2", author2, List.of(genre3, genre4))).block();
        book3 = bookRepository.save(new Book("3", "BookTitle_3", author3, List.of(genre5, genre6))).block();
    }

    @ChangeSet(order = "005", id = "insertComments", author = "boris.osechinskiy")
    public void insertComments(CommentRepository commentRepository) {
        commentRepository.save(new Comment("1", book1, "First Comment")).block();
        commentRepository.save(new Comment("2", book2, "Second Comment")).block();
        commentRepository.save(new Comment("3", book3, "Third Comment")).block();
    }
}
