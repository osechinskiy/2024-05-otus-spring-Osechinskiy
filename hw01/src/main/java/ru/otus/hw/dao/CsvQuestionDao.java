package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileReader;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private static final int LINE_SKIP_COUNT = 1;

    private static final char SEPARATOR = ';';

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        List<QuestionDto> questionsDto;
        try {
            URL resource = getClass().getClassLoader().getResource(fileNameProvider.getTestFileName());
            questionsDto = new CsvToBeanBuilder(new FileReader(resource.getFile()))
                    .withType(QuestionDto.class)
                    .withSkipLines(LINE_SKIP_COUNT)
                    .withSeparator(SEPARATOR)
                    .build()
                    .parse();
        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
        if (CollectionUtils.isNotEmpty(questionsDto)) {
            questionsDto.forEach(question -> questions.add(question.toDomainObject()));
        }

        return questions;
    }
}
