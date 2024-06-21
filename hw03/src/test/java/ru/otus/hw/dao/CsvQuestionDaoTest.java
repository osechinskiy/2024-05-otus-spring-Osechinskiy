package ru.otus.hw.dao;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.Application;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
@TestPropertySource(locations =  "classpath:test-application.properties")
@DisplayName("DAO CsvQuestionDao")
class CsvQuestionDaoTest {

    @Autowired
    TestFileNameProvider testFileNameProvider;
    @Autowired
    CsvQuestionDao csvQuestionDao;

    @Test
    @DisplayName("Получение данных из ДАО")
    void readCsvFile() {
        List<Question> questions = csvQuestionDao.findAll();

        assertAll(
                () -> assertEquals(5, questions.size()),
                () -> questions.forEach(question -> assertNotNull(question.text())),
                () -> questions.forEach(question -> assertNotNull(question.answers()))
        );
    }

}