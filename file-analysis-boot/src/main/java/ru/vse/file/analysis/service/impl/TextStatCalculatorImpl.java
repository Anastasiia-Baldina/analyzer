package ru.vse.file.analysis.service.impl;

import ru.vse.file.analysis.service.TextStatCalculator;
import ru.vse.file.analysis.service.TextStats;

import java.util.regex.*;

public class TextStatCalculatorImpl implements TextStatCalculator {
    private static final Pattern pattern = Pattern.compile("[a-zA-Zа-яА-Я]+");

    @Override
    public TextStats calculate(String text) {
        return new TextStats(
                getWordCount(text),
                getParagraphCount(text),
                text.length());
    }

    private static int getWordCount(String text) {
        if (text.isBlank()) {
            return 0;
        }
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int getParagraphCount(String text) {
        if (text.isBlank()) {
            return 0;
        }
        String[] parts = text.split("\n");
        int count = 0;
        for (var part : parts) {
            if (!part.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(
                new TextStatCalculatorImpl().calculate("Привет! Как дела ?\nПривет! Все отлично.")
        );
        System.out.println(
                new TextStatCalculatorImpl().calculate("Hello, how are you?")
        );
    }
}
