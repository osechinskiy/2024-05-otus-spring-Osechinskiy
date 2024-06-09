package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();
        if (CollectionUtils.isEmpty(questions)) {
            ioService.printLine("No questions found");
            return;
        }
        questions.forEach(question -> {
            ioService.printFormattedLine("Question: %s", question.text());
            if (CollectionUtils.isNotEmpty(question.answers())) {
                question.answers().forEach(answer -> ioService.printLine(answer.text()));
            }
            ioService.printLine("");
        });

    }
}
