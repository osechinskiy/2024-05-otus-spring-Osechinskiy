package ru.otus.hw.mongok.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

@ChangeLog
public class DatabaseChangelog {
    private MongoAuthor author1;

    private MongoAuthor author2;

    private MongoAuthor author3;

    private MongoGenre genre1;

    private MongoGenre genre2;

    private MongoGenre genre3;

    private MongoGenre genre4;

    private MongoGenre genre5;

    private MongoGenre genre6;

    private MongoBook book1;

    private MongoBook book2;

    private MongoBook book3;
    
    @ChangeSet(order = "001", id = "dropDb", author = "boris.osechinskiy", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "boris.osechinskiy")
    public void insertAuthors(MongoAuthorRepository authorRepository) {
        author1 = authorRepository.save(new MongoAuthor("1", "Author_1"));
        author2 = authorRepository.save(new MongoAuthor("2", "Author_2"));
        author3 = authorRepository.save(new MongoAuthor("3", "Author_3"));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "boris.osechinskiy")
    public void insertGenres(MongoGenreRepository genreRepository) {
        genre1 = genreRepository.save(new MongoGenre("1", "Genre_1"));
        genre2 = genreRepository.save(new MongoGenre("2", "Genre_2"));
        genre3 = genreRepository.save(new MongoGenre("3", "Genre_3"));
        genre4 = genreRepository.save(new MongoGenre("4", "Genre_4"));
        genre5 = genreRepository.save(new MongoGenre("5", "Genre_5"));
        genre6 = genreRepository.save(new MongoGenre("6", "Genre_6"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "boris.osechinskiy")
    public void insertBooks(MongoBookRepository bookRepository) {
        book1 = bookRepository.save(new MongoBook("1", "BookTitle_1", author1, genre1));
        book2 = bookRepository.save(new MongoBook("2", "BookTitle_2", author2, genre3));
        book3 = bookRepository.save(new MongoBook("3", "BookTitle_3", author3, genre5));
    }

    @ChangeSet(order = "005", id = "insertComments", author = "boris.osechinskiy")
    public void insertComments(MongoCommentRepository commentRepository) {
        commentRepository.save(new MongoComment("1", book1, "First Comment"));
        commentRepository.save(new MongoComment("2", book2, "Second Comment"));
        commentRepository.save(new MongoComment("3", book3, "Third Comment"));
    }
}
