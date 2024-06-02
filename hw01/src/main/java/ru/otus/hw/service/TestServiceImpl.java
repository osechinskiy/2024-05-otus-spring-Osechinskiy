package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        try {
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
        } catch (QuestionReadException e) {
            ioService.printFormattedLine("Was an error loading questions %s", e.getMessage());
        }
    }
}
