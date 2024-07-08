package ru.otus.hw.repositories;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        String sql = """
            select
                b.id as book_id, b.title as book_title,
                a.id as author_id, a.full_name as author_name,
                g.id as genre_id, g.name as genre_name
            from books b
            inner join authors a on b.author_id = a.id
            inner join books_genres bg on bg.book_id = b.id
            inner join genres g on g.id = bg.genre_id
            where b.id = :id
        """;
        Book book = jdbc.query(sql, Map.of("id", id), new BookResultSetExtractor());
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books b where b.id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        String query = """
                   select
                       b.id as book_id, b.title as book_title,
                       a.id as author_id, a.full_name as author_name
                   from books b
                   inner join authors a on b.author_id = a.id
                """;
        return jdbc.getJdbcOperations().query(query, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.getJdbcOperations()
                .query("select book_id, genre_id from books_genres", new BookGenreRelationMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
            List<BookGenreRelation> relations) {
        Map<Long, Book> bookById = booksWithoutGenres.stream()
                .collect(Collectors.toMap(Book::getId, book -> book));
        for (var relation : relations) {
            var book = bookById.get(relation.bookId());
            if (book.getGenres() == null) {
                book.setGenres(new ArrayList<>());
            }

            Genre genre = genres.stream().filter(g -> g.getId() == relation.genreId).findFirst().orElse(null);
            book.getGenres().add(genre);
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("author_id", book.getAuthor().getId());

        String query = "insert into books (title, author_id) values (:title, :author_id)";
        jdbc.update(query, parameterSource, keyHolder, new String[] {"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", book.getId());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("author_id", book.getAuthor().getId());

        String query = "update books set title = :title, author_id = :author_id where id = :id";

        int count = jdbc.update(query, parameterSource);

        if (count < 1) {
            throw new EntityNotFoundException("Can't find book with id %s to update".formatted(book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        String query = "insert into books_genres (book_id, genre_id) values (?, ?)";

        jdbc.getJdbcOperations().batchUpdate(query, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var genre = book.getGenres().get(i);
                ps.setLong(1, book.getId());
                ps.setLong(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return book.getGenres().size();
            }
        });
    }

    private void removeGenresRelationsFor(Book book) {
        jdbc.update("delete books_genres where book_id", Map.of("id", book.getId()));
    }

    private static class BookGenreRelationMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
        }
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));

            var author = new Author();
            author.setId(rs.getLong("author_id"));
            author.setFullName(rs.getString("full_name"));

            book.setAuthor(author);

            return book;
        }
    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = new Book();
            while (rs.next()) {
                if (book.getId() == 0L) {
                    book.setId(rs.getLong("book_id"));
                    book.setTitle(rs.getString("book_title"));
                    Author author = new Author(rs.getLong("author_id"), rs.getString("author_name"));
                    book.setAuthor(author);
                    book.setGenres(new ArrayList<>());
                }
                book.getGenres().add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
            return book.getId() != 0L ? book : null;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {

    }
}
