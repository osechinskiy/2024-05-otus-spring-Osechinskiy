package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
    void executeTestWithError() {
        when(questionDao.findAll()).thenThrow(QuestionReadException.class);
        try {
            testService.executeTest();
        } catch (Exception e) {
            assertInstanceOf(QuestionReadException.class, e);
        }
    }
}