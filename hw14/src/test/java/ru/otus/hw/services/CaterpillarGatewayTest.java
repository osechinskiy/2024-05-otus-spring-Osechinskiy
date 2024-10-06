package ru.otus.hw.services;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CaterpillarGatewayTest {

    @Autowired
    CaterpillarGateway caterpillarGateway;

    @Test
    void caterpillarGatewayTest() {
        List<Butterfly> butterflies = caterpillarGateway.process(generateCaterpillars());

        assertAll(
                () -> assertNotNull(butterflies),
                () -> assertNotNull(butterflies.get(0)),
                () -> assertEquals(5, butterflies.size())
        );
    }

    private List<Caterpillar> generateCaterpillars() {
        return List.of(
                new Caterpillar(1, 1),
                new Caterpillar(2, 2),
                new Caterpillar(3, 3),
                new Caterpillar(4, 4),
                new Caterpillar(5, 5)
        );
    }
}