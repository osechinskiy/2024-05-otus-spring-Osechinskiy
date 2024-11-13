package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Comment;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findByBookId(String bookId);

}