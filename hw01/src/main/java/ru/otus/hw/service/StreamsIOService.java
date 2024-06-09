package ru.otus.hw.service;

import java.io.PrintStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StreamsIOService implements IOService {
    private final PrintStream printStream;

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }
}
