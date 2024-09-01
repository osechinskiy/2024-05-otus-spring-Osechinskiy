package ru.otus.hw.rest.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {

    private boolean hasError;

    private Map<String, String> badFields;
}
