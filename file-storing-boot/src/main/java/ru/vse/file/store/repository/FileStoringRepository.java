package ru.vse.file.store.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.vse.file.store.dao.FileDao;

public interface FileStoringRepository {
    @Nullable
    FileDao findById(String id);

    void insert(@NotNull FileDao fileDao);
}
