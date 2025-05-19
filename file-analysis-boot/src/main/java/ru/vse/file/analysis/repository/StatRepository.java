package ru.vse.file.analysis.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.vse.file.analysis.dao.StatDao;

import java.util.List;

public interface StatRepository {

    void insert(@NotNull StatDao statDao);

    @Nullable
    StatDao findById(String fileId);

    @NotNull
    List<String> findByHash(String fileHash);
}
