package ru.otus.hw.config;

import java.util.random.RandomGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.ButterflyColor;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Chrysalis;

@Configuration
public class IntegrationConfig {

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    @Bean
    public MessageChannelSpec<?, ?> caterpillarChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> butterflyChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow faunaFlow() {
        return IntegrationFlow.from(caterpillarChannel())
                .split()
                .<Caterpillar, Chrysalis>transform(caterpillar -> new Chrysalis(
                        caterpillar.getLength() + 5,
                        randomGenerator.nextInt(6, 12))
                )
                .<Chrysalis, Butterfly>transform(chrysalis -> new Butterfly(
                        randomGenerator.nextInt(chrysalis.getLength() * 10, 400),
                        randomGenerator.nextInt(chrysalis.getLength() + 12, chrysalis.getDiameter() * 10),
                        ButterflyColor.values()[randomGenerator.nextInt(ButterflyColor.values().length)]
                ))
                .<Butterfly>log(LoggingHandler.Level.INFO, "Butterfly", Message::getPayload)
                .aggregate()
                .channel(butterflyChannel())
                .get();
    }
}
