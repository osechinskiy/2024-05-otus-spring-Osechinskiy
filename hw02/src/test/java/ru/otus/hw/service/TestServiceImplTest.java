package ru.otus.hw.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис TestServiceImpl")
class TestServiceImplTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @InjectMocks
    private TestServiceImpl testService;

    @Test
    @DisplayName("Выбрасывание QuestionReadException")
    void executeTestWithError() {
        Student student = new Student("Ivan", "Ivanov");
        when(questionDao.findAll()).thenThrow(QuestionReadException.class);
        assertThrows(QuestionReadException.class, () -> testService.executeTestFor(student));
    }

    @Test
    @DisplayName("Кол-во правильных ответов увеличивается")
    void executeTestResultWithValidAnswer() {
        Student student = new Student("Ivan", "Ivanov");
        String validAnswer = "Science doesn't know this yet";
        when(questionDao.findAll()).thenReturn(mockQuestions());
        when(ioService.readStringWithPrompt(any())).thenReturn(validAnswer);
        TestResult result = testService.executeTestFor(student);

        assertEquals(1, result.getRightAnswersCount());
    }

    @Test
    @DisplayName("Кол-во правильных ответов не увеличивается")
    void executeTestResultWithInvalidAnswer() {
        Student student = new Student("Ivan", "Ivanov");
        String invalidAnswer = "Certainly. The red UFO is from Mars. And green is from Venus";
        when(questionDao.findAll()).thenReturn(mockQuestions());
        when(ioService.readStringWithPrompt(any())).thenReturn(invalidAnswer);
        TestResult result = testService.executeTestFor(student);

        assertEquals(0, result.getRightAnswersCount());
    }

    private List<Question> mockQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Is there life on Mars?",
                        List.of(new Answer("Science doesn't know this yet", true),
                                new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false))));
        return questions;
    }
}