package ru.otus.hw.config;

import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.processors.BookProcessor;
import ru.otus.hw.processors.CommentProcessor;
import ru.otus.hw.processors.GenreProcessor;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private static final int CHUNK_SIZE = 5;

    private static final String MIGRATE_H2_TO_MONGO_JOB_NAME = "migrateMongoToH2Job";

    private static final String CREATE_TEMPORARY_AUTHOR_MONGO_TO_H2_ID_TABLE_NAME =
            "createTemporaryAuthorMongoToH2IdTable";

    private static final String CREATE_TEMPORARY_BOOK_MONGO_TO_H2_ID_TABLE_NAME =
            "createTemporaryBookMongoToH2IdTable";

    private static final String CREATE_TEMPORARY_COMMENT_MONGO_TO_H2_ID_TABLE_NAME =
            "createTemporaryCommentMongoToH2IdTable";

    private static final String CREATE_TEMPORARY_GENRE_MONGO_TO_H2_ID_TABLE_NAME =
            "createTemporaryGenreMongoToH2IdTable";

    private static final String AUTHOR_MIGRATION_STEP_NAME = "authorMigrationStep";

    private static final String BOOK_MIGRATION_STEP_NAME = "bookMigrationStep";

    private static final String COMMENT_MIGRATION_STEP_NAME = "commentMigrationStep";

    private static final String GENRE_MIGRATION_STEP_NAME = "genreMigrationStep";

    private static final String AUTHOR_READER_NAME = "authorReader";

    private static final String BOOK_READER_NAME = "bookReader";

    private static final String COMMENT_READER_NAME = "commentReader";

    private static final String GENRE_READER_NAME = "genreReader";

    private static final String CREATE_TEMPORARY_MONGO_AUTHOR_FOR_ID_TABLE_H2_SQL =
            "CREATE TABLE temp_author_mongo_h2_ids (id_mongo VARCHAR(255) not null, id_h2 bigserial not null )";

    private static final String CREATE_TEMPORARY_BOOK_MONGO_TO_H2_ID_TABLE_SQL =
            "CREATE TABLE temp_book_mongo_h2_ids (id_mongo VARCHAR(255) not null, id_h2 bigserial not null)";

    private static final String CREATE_TEMPORARY_COMMENT_MONGO_TO_H2_ID_TABLE_SQL =
            "CREATE TABLE temp_comment_mongo_h2_ids (id_mongo VARCHAR(255) not null, id_h2 bigserial not null)";

    private static final String CREATE_TEMPORARY_GENRE_MONGO_TO_H2_ID_TABLE_SQL =
            "CREATE TABLE temp_genre_mongo_h2_ids (id_mongo VARCHAR(255) not null, id_h2 bigserial not null)";

    private static final String DROP_TEMPORARY_AUTHOR_MONGO_TO_H2_ID_TABLE_SQL =
            "DROP TABLE temp_author_mongo_h2_ids";

    private static final String DROP_TEMPORARY_BOOK_MONGO_TO_H2_ID_TABLE_SQL =
            "DROP TABLE temp_book_mongo_h2_ids";

    private static final String DROP_TEMPORARY_COMMENT_MONGO_TO_H2_ID_TABLE_SQL =
            "DROP TABLE temp_comment_mongo_h2_ids";

    private static final String DROP_TEMPORARY_GENRE_MONGO_TO_H2_ID_TABLE_SQL =
            "DROP TABLE temp_genre_mongo_h2_ids";

    private static final String FIND_ALL = "findAll";

    private static final String INSERT_INTO_TEMP_AUTHOR_MONGO_H2_IDS_SQL =
            "INSERT INTO temp_author_mongo_h2_ids(id_mongo, id_h2) VALUES (:id, SELECT NEXT VALUE FOR SEQ_AUTHORS)";

    private static final String INSERT_INTO_TEMP_GENRE_MONGO_H2_IDS_SQL =
            "INSERT INTO temp_genre_mongo_h2_ids(id_mongo, id_h2) VALUES (:id, SELECT NEXT VALUE FOR SEQ_GENRES)";

    private static final String INSERT_INTO_TEMP_BOOK_MONGO_H2_IDS_SQL =
            "INSERT INTO temp_book_mongo_h2_ids(id_mongo, id_h2) VALUES (:id, SELECT NEXT VALUE FOR SEQ_BOOKS)";

    private static final String INSERT_INTO_TEMP_COMMENT_MONGO_H2_IDS_SQL =
            "INSERT INTO temp_comment_mongo_h2_ids(id_mongo, id_h2) VALUES (:id, SELECT NEXT VALUE FOR SEQ_COMMENTS)";

    private static final String INSERT_INTO_AUTHORS_SQL =
            "INSERT INTO authors(id, full_name) "
                    + "VALUES ((SELECT id_h2 FROM temp_author_mongo_h2_ids WHERE id_mongo = :id), :fullName)";

    private static final String INSERT_INTO_GENRES_SQL =
            "INSERT INTO genres(id, name) VALUES ((SELECT id_h2 FROM temp_genre_mongo_h2_ids "
                    + "WHERE id_mongo = :id), :name)";

    private static final String INSERT_INTO_BOOKS_SQL =
            "INSERT INTO books(title, id, author_id, genre_id) VALUES (?, " +
                    "(SELECT id_h2 FROM temp_book_mongo_h2_ids WHERE id_mongo = ?), " +
                    "(SELECT id_h2 FROM temp_author_mongo_h2_ids WHERE id_mongo = ?), " +
                    "(SELECT id_h2 FROM temp_genre_mongo_h2_ids WHERE id_mongo = ?))";


    private static final String INSERT_INTO_COMMENTS_SQL =
            "INSERT INTO comments(text, id, book_id) " +
                    "VALUES (?, (SELECT id_h2 FROM temp_comment_mongo_h2_ids WHERE id_mongo = ?), " +
                    "(SELECT id_h2 FROM temp_book_mongo_h2_ids WHERE id_mongo = ?))";

    private static final String DROP_TEMPORARY_AUTHOR_MONGO_TO_H2_ID_TABLE_NAME =
            "dropTemporaryAuthorMongoToH2IdTable";

    private static final String DROP_TEMPORARY_BOOK_MONGO_TO_H2_ID_TABLE_NAME =
            "dropTemporaryBookMongoToH2IdTable";

    private static final String DROP_TEMPORARY_COMMENT_MONGO_TO_H2_ID_TABLE_NAME =
            "dropTemporaryCommentMongoToH2IdTable";

    private static final String DROP_TEMPORARY_GENRE_MONGO_TO_H2_ID_TABLE_NAME =
            "dropTemporaryGenreMongoToH2IdTable";



    private final MongoAuthorRepository mongoAuthorRepository;

    private final MongoBookRepository mongoBookRepository;

    private final MongoGenreRepository mongoGenreRepository;

    private final MongoCommentRepository mongoCommentRepository;

    private final JobRepository jobRepository;

    private final DataSource dataSource;

    private final PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job migrateMongoToH2Job(Step authorMigrationStep, Step genreMigrationStep, Step bookMigrationStep,
            Step commentMigrationStep) {
        return new JobBuilder(MIGRATE_H2_TO_MONGO_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createTemporaryAuthorMongoToH2IdTable())
                .next(createTemporaryGenreMongoToH2IdTable())
                .next(createTemporaryBookMongoToH2IdTable())
                .next(createTemporaryCommentMongoToH2IdTable())
                .next(authorMigrationStep)
                .next(genreMigrationStep)
                .next(bookMigrationStep)
                .next(commentMigrationStep)
                .next(dropTemporaryAuthorMongoToH2IdTable())
                .next(dropTemporaryGenreMongoToH2IdTable())
                .next(dropTemporaryBookMongoToH2IdTable())
                .next(dropTemporaryCommentMongoToH2IdTable())
                .build();
    }

    @Bean
    public TaskletStep createTemporaryAuthorMongoToH2IdTable() {
        return new StepBuilder(CREATE_TEMPORARY_AUTHOR_MONGO_TO_H2_ID_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(CREATE_TEMPORARY_MONGO_AUTHOR_FOR_ID_TABLE_H2_SQL);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep createTemporaryBookMongoToH2IdTable() {
        return new StepBuilder(CREATE_TEMPORARY_BOOK_MONGO_TO_H2_ID_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(CREATE_TEMPORARY_BOOK_MONGO_TO_H2_ID_TABLE_SQL);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep createTemporaryCommentMongoToH2IdTable() {
        return new StepBuilder(CREATE_TEMPORARY_COMMENT_MONGO_TO_H2_ID_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(CREATE_TEMPORARY_COMMENT_MONGO_TO_H2_ID_TABLE_SQL);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep createTemporaryGenreMongoToH2IdTable() {
        return new StepBuilder(CREATE_TEMPORARY_GENRE_MONGO_TO_H2_ID_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(CREATE_TEMPORARY_GENRE_MONGO_TO_H2_ID_TABLE_SQL);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoAuthor> authorReader() {
        return new RepositoryItemReaderBuilder<MongoAuthor>()
                .name(AUTHOR_READER_NAME)
                .repository(mongoAuthorRepository)
                .methodName(FIND_ALL)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<AuthorDto> authorInsertTempTable() {
        JdbcBatchItemWriter<AuthorDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(INSERT_INTO_TEMP_AUTHOR_MONGO_H2_IDS_SQL);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<AuthorDto> authorJdbcBatchItemWriter() {
        JdbcBatchItemWriter<AuthorDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(INSERT_INTO_AUTHORS_SQL);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<AuthorDto> compositeAuthorWriter(
            JdbcBatchItemWriter<AuthorDto> authorInsertTempTable,
            JdbcBatchItemWriter<AuthorDto> authorJdbcBatchItemWriter) {

        CompositeItemWriter<AuthorDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(authorInsertTempTable, authorJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step authorMigrationStep(RepositoryItemReader<MongoAuthor> reader,
            CompositeItemWriter<AuthorDto> writer, AuthorProcessor processor) {
        return new StepBuilder(AUTHOR_MIGRATION_STEP_NAME, jobRepository)
                .<MongoAuthor, AuthorDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoGenre> genreReader() {
        return new RepositoryItemReaderBuilder<MongoGenre>()
                .name(GENRE_READER_NAME)
                .repository(mongoGenreRepository)
                .methodName(FIND_ALL)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<GenreDto> genreInsertTempTable() {
        JdbcBatchItemWriter<GenreDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(INSERT_INTO_TEMP_GENRE_MONGO_H2_IDS_SQL);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<GenreDto> genreJdbcBatchItemWriter() {
        JdbcBatchItemWriter<GenreDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(INSERT_INTO_GENRES_SQL);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<GenreDto> compositeGenreWriter(
            JdbcBatchItemWriter<GenreDto> genreInsertTempTable,
            JdbcBatchItemWriter<GenreDto> genreJdbcBatchItemWriter) {

        CompositeItemWriter<GenreDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(genreInsertTempTable, genreJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step genreMigrationStep(RepositoryItemReader<MongoGenre> reader,
            CompositeItemWriter<GenreDto> writer, GenreProcessor processor) {
        return new StepBuilder(GENRE_MIGRATION_STEP_NAME, jobRepository)
                .<MongoGenre, GenreDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoBook> bookReader() {
        return new RepositoryItemReaderBuilder<MongoBook>()
                .name(BOOK_READER_NAME)
                .repository(mongoBookRepository)
                .methodName(FIND_ALL)
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BookDto> bookInsertTempTable() {
        JdbcBatchItemWriter<BookDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(INSERT_INTO_TEMP_BOOK_MONGO_H2_IDS_SQL);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter() {
        JdbcBatchItemWriter<BookDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemPreparedStatementSetter((bookDto, statement) -> {
            statement.setString(1, bookDto.getTitle());
            statement.setString(2, bookDto.getId());
            statement.setString(3, bookDto.getAuthorId());
            statement.setString(4, bookDto.getGenreId());
        });
        writer.setSql(INSERT_INTO_BOOKS_SQL);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<BookDto> compositeBookWriter(
            JdbcBatchItemWriter<BookDto> bookInsertTempTable,
            JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter) {

        CompositeItemWriter<BookDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(bookInsertTempTable, bookJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step bookMigrationStep(RepositoryItemReader<MongoBook> reader,
            CompositeItemWriter<BookDto> writer, BookProcessor processor) {
        return new StepBuilder(BOOK_MIGRATION_STEP_NAME, jobRepository)
                .<MongoBook, BookDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoComment> commentReader() {
        return new RepositoryItemReaderBuilder<MongoComment>()
                .name(COMMENT_READER_NAME)
                .repository(mongoCommentRepository)
                .methodName(FIND_ALL)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CommentDto> commentInsertTempTable() {
        JdbcBatchItemWriter<CommentDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(INSERT_INTO_TEMP_COMMENT_MONGO_H2_IDS_SQL);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<CommentDto> commentJdbcBatchItemWriter() {
        JdbcBatchItemWriter<CommentDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemPreparedStatementSetter((commentDto, statement) -> {
            statement.setString(1, commentDto.getText());
            statement.setString(2, commentDto.getId());
            statement.setString(3, commentDto.getBookId());
        });
        writer.setSql(INSERT_INTO_COMMENTS_SQL);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<CommentDto> compositeCommentWriter(
            JdbcBatchItemWriter<CommentDto> commentInsertTempTable,
            JdbcBatchItemWriter<CommentDto> commentJdbcBatchItemWriter) {

        CompositeItemWriter<CommentDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(commentInsertTempTable, commentJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step commentMigrationStep(RepositoryItemReader<MongoComment> reader,
            CompositeItemWriter<CommentDto> writer, CommentProcessor processor) {
        return new StepBuilder(COMMENT_MIGRATION_STEP_NAME, jobRepository)
                .<MongoComment, CommentDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public TaskletStep dropTemporaryAuthorMongoToH2IdTable() {
        return new StepBuilder(DROP_TEMPORARY_AUTHOR_MONGO_TO_H2_ID_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(DROP_TEMPORARY_AUTHOR_MONGO_TO_H2_ID_TABLE_SQL);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTemporaryBookMongoToH2IdTable() {
        return new StepBuilder(DROP_TEMPORARY_BOOK_MONGO_TO_H2_ID_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(DROP_TEMPORARY_BOOK_MONGO_TO_H2_ID_TABLE_SQL);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTemporaryCommentMongoToH2IdTable() {
        return new StepBuilder(DROP_TEMPORARY_COMMENT_MONGO_TO_H2_ID_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(DROP_TEMPORARY_COMMENT_MONGO_TO_H2_ID_TABLE_SQL);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTemporaryGenreMongoToH2IdTable() {
        return new StepBuilder(DROP_TEMPORARY_GENRE_MONGO_TO_H2_ID_TABLE_NAME, jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(DROP_TEMPORARY_GENRE_MONGO_TO_H2_ID_TABLE_SQL);
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }
}
