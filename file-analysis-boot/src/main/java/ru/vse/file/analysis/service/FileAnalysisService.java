package ru.vse.file.analysis.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.vse.file.dto.IdDto;
import ru.vse.file.dto.StatisticsDto;

public interface FileAnalysisService {
    @Nullable
    StatisticsDto analyze(@NotNull IdDto idDto);

    @NotNull
    byte[] getWordCloudImage(@NotNull IdDto idDto);
}
