package ru.vse.file.analysis.service;

import org.jetbrains.annotations.NotNull;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;

public interface FileStoreClient {
    @NotNull
    FileDto findById(IdDto idDto);
}
