package ru.otus.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppProperties implements TestConfig, TestFileNameProvider {

    @Value("${test.rightAnswersCountToPass:3}")
    private int rightAnswersCountToPass;

    @Value("${test.fileName:questions.csv}")
    private String testFileName;
}
