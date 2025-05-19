package ru.vse.file.analysis.service;

import org.jetbrains.annotations.NotNull;

public interface HashCalculator {
    @NotNull
    String calculate(String text);
}
