package ru.otus.hw.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            String validAnswer = null;
            ioService.printFormattedLine("Question: %s", question.text());
            if (CollectionUtils.isNotEmpty(question.answers())) {
                validAnswer = printAnswersIfPossible(question.answers());
            }
            ioService.printLine("");
            var answer = ioService.readStringWithPrompt("Your answer is:");
            var isAnswerValid = StringUtils.equals(answer, validAnswer);
            testResult.applyAnswer(question, isAnswerValid);
            ioService.printLine("");
        }
        return testResult;
    }

    private String printAnswersIfPossible(Collection<Answer> answers) {
        String correctAnswer = null;
        for (var answer : answers) {
            ioService.printLine(answer.text());
            if (answer.isCorrect()) {
                correctAnswer = answer.text();
            }
        }
        return correctAnswer;
    }
}
