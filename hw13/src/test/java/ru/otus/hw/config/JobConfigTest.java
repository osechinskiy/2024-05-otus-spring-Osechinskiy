package ru.otus.hw.config;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaComment;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
class JobConfigTest {

    private static final String IMPORT_USER_JOB_NAME = "migrateMongoToH2Job";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_USER_JOB_NAME);

        JobParameters parameters = new JobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        List<JpaAuthor> authors = jpaAuthorRepository.findAll();
        assertThat(authors)
                .isNotEmpty()
                .isEqualTo(getExpectedAuthors());

        List<JpaGenre> genres = jpaGenreRepository.findAll();
        assertThat(genres)
                .isNotEmpty()
                .isEqualTo(getExpectedGenres());

        List<JpaBook> books = jpaBookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .isEqualTo(getExpectedBooks());

        List<JpaComment> comments = jpaCommentRepository.findAll();
        assertThat(comments)
                .isNotEmpty()
                .isEqualTo(getExpectedComments());
    }

    private List<JpaAuthor> getExpectedAuthors() {
        return List.of(
                new JpaAuthor(1L, "Author_1"),
                new JpaAuthor(2L, "Author_2"),
                new JpaAuthor(3L, "Author_3")
        );
    }

    private List<JpaGenre> getExpectedGenres() {
        return List.of(
                new JpaGenre(1L, "Genre_1"),
                new JpaGenre(2L, "Genre_2"),
                new JpaGenre(3L, "Genre_3"),
                new JpaGenre(4L, "Genre_4"),
                new JpaGenre(5L, "Genre_5"),
                new JpaGenre(6L, "Genre_6")
        );
    }

    private List<JpaBook> getExpectedBooks() {
        List<JpaAuthor> authors = getExpectedAuthors();
        List<JpaGenre> genres = getExpectedGenres();

        return List.of(
                new JpaBook(1L, "BookTitle_1", authors.get(0), genres.get(0)),
                new JpaBook(2L, "BookTitle_2", authors.get(1), genres.get(1)),
                new JpaBook(3L, "BookTitle_3", authors.get(2), genres.get(2))
        );
    }

    private List<JpaComment> getExpectedComments() {
        List<JpaBook> books = getExpectedBooks();
        return List.of(
                new JpaComment(1L, books.get(0), "First Comment"),
                new JpaComment(2L, books.get(1), "Second Comment"),
                new JpaComment(3L, books.get(2), "Third Comment")
        );
    }
}