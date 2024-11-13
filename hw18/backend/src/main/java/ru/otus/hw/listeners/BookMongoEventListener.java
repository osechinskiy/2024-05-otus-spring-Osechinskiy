package ru.otus.hw.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Component
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {
    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        String bookId = String.valueOf(event.getSource().get("_id"));
        commentRepository.deleteAll(commentRepository.findByBookId(bookId));
    }
}
