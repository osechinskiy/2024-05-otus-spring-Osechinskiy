package ru.otus.hw.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String ERROR_MESSAGE = "select an answer option from the list";

    private static final int MIN_ANSWER_NUMBER = 1;

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printFormattedLine("Question: %s", question.text());
            var isAnswerValid = processAskQuestionAndReadAnswer(question.answers());
            ioService.printLine("");
            testResult.applyAnswer(question, isAnswerValid);
            ioService.printLine("");
        }

        return testResult;
    }

    private boolean processAskQuestionAndReadAnswer(Collection<Answer> answers) {
        var validAnswer = printAnswers(answers);
        var userAnswer = readUserAnswer(answers);

        return userAnswer.text().equals(validAnswer);
    }

    private String printAnswers(Collection<Answer> answers) {
        String correctAnswer = null;
        int number = 1;
        for (var answer : answers) {
            ioService.printFormattedLine("%d) %s", number, answer.text());
            if (answer.isCorrect()) {
                correctAnswer = answer.text();
            }
            number++;
        }
        return correctAnswer;
    }

    private Answer readUserAnswer(Collection<Answer> answers) {
        var userAnswer = ioService.readIntForRangeWithPrompt(MIN_ANSWER_NUMBER, answers.size(), "Chose answer:",
                ERROR_MESSAGE);
        return answers.stream().toList().get(userAnswer - 1);
    }
}
