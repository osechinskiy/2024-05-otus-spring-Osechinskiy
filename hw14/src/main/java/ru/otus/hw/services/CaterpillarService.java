package ru.otus.hw.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.random.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Caterpillar;

@Service
@RequiredArgsConstructor
public class CaterpillarService {

    private final CaterpillarGateway caterpillarGateway;

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    public void startGenerateCaterpillarLoop() {
        ForkJoinPool.commonPool().execute(() -> {
            List<Caterpillar> caterpillars = generateCaterpillars();
            caterpillarGateway.process(caterpillars);
        });
    }

    private List<Caterpillar> generateCaterpillars() {
        int caterpillarsCount = randomGenerator.nextInt(50, 100);
        List<Caterpillar> caterpillars = new ArrayList<>(caterpillarsCount);
        for (int i = 0; i < caterpillarsCount; i++) {
            caterpillars.add(generateCaterpillar());
        }
        return caterpillars;
    }

    private Caterpillar generateCaterpillar() {
        int weight = randomGenerator.nextInt(1, 10);
        int length = randomGenerator.nextInt(5, 30);
        return new Caterpillar(weight, length);
    }

}
