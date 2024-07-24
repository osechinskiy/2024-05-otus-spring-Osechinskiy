package ru.otus.hw.repositories;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать комментарий по id")
    @Test
    @Order(1)
    void shouldReturnCorrectCommentById() {
        var actualComment = commentRepository.findById("1");
        var expectedComment = mongoTemplate.findById("1", Comment.class);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарий по book id")
    @Test
    @Order(2)
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = commentRepository.findByBookId("1");
        var expectedComments = getDbComments();

        assertThat(actualComments).containsExactlyElementsOf(List.of(expectedComments.get(0)));
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    @Order(3)
    void shouldSaveNewComment() {
        var book = mongoTemplate.findById("1", Book.class);
        var newComment = new Comment(null, book, "New Comment");

        commentRepository.save(newComment);
        Comment actualComment = mongoTemplate.findById(newComment.getId(), Comment.class);

        assertThat(actualComment).isEqualTo(newComment);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    @Order(4)
    void shouldUpdateComment() {
        var comment = mongoTemplate.findById("1", Comment.class);
        comment.setText("updated text");

        commentRepository.save(comment);
        var actualComment = mongoTemplate.findById(comment.getId(), Comment.class);

        assertThat(actualComment).isEqualTo(comment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    @Order(5)
    void shouldDeleteComment() {
        var comment = mongoTemplate.findById("1", Comment.class);
        commentRepository.deleteById(comment.getId());
        var deletedComment = mongoTemplate.findById(comment.getId(), Comment.class);
        assertThat(deletedComment).isNull();
    }

    private List<Comment> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id ->mongoTemplate.findById(String.valueOf(id), Comment.class))
                .toList();
    }
}
