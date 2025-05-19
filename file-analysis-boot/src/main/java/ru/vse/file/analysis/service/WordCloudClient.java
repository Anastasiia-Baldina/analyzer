package ru.vse.file.analysis.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WordCloudClient {
    @NotNull
    byte[] createPicture(String text);
}
