package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Butterfly {

    private int length;

    private int weight;

    private ButterflyColor color;
}
