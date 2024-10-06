package ru.otus.hw.services;

import java.util.Collection;
import java.util.List;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;

@MessagingGateway
public interface CaterpillarGateway {

    @Gateway(requestChannel  = "caterpillarChannel", replyChannel = "butterflyChannel")
    List<Butterfly> process(Collection<Caterpillar> caterpillars);
}
