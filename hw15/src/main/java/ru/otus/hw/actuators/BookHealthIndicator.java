package ru.otus.hw.actuators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

@Component("BookHealth")
@RequiredArgsConstructor
public class BookHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        List<Book> books = bookService.findAll();
        if (CollectionUtils.isNotEmpty(books)) {
            Map<String, Object> details = new HashMap<>();
            details.put("message", "С книгами все в порядке!");
            details.put("booksSize", books.size());
            return Health.up()
                    .withDetails(details)
                    .build();
        }
        return Health.down()
                .withDetail("message", "В библиотеке нету книг!")
                .build();
    }
}
