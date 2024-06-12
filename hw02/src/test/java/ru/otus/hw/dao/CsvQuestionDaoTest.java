package ru.otus.hw.dao;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DAO CsvQuestionDao")
class CsvQuestionDaoTest {

    @Mock
    private AppProperties appProperties;
    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Test
    @DisplayName("Получение данных из ДАО")
    void readCsvFile() {
        when(appProperties.getTestFileName()).thenReturn("test-questions.csv");
        List<Question> questions = csvQuestionDao.findAll();

        assertEquals(5, questions.size());
    }

}