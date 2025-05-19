package ru.vse.file.store.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;

public interface FileStoringService {

    IdDto save(@NotNull FileDto fileDto);

    @Nullable
    FileDto get(@NotNull IdDto idDto);
}
