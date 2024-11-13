package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorResponse {

    private long id;

    private String fullName;
}
