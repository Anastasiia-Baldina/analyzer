package ru.vse.file.store.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;
import ru.vse.file.store.dao.FileDao;
import ru.vse.file.store.repository.FileStoringRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileStoringServiceImpl implements FileStoringService {
    private final FileStoringRepository repository;
    private final String fileDir;

    public FileStoringServiceImpl(FileStoringRepository repository, String fileDir) {
        this.repository = repository;
        this.fileDir = fileDir;
    }

    @Override
    public IdDto save(@NotNull FileDto fileDto) {
        var id = UUID.randomUUID().toString();
        var fileDao = FileDao.builder()
                .setId(id)
                .setDir(fileDir)
                .setName(fileDto.getFilename())
                .build();
        saveToFile(fileDao, fileDto.getText());
        repository.insert(fileDao);

        return new IdDto().setValue(fileDao.id());
    }

    @Nullable
    @Override
    public FileDto get(@NotNull IdDto idDto) {
        var fileDao = repository.findById(idDto.getValue());
        if (fileDao == null) {
            return null;
        }
        return new FileDto()
                .setFilename(fileDao.name())
                .setText(readFromFile(fileDao));
    }

    private static void saveToFile(FileDao fileDao, String text) {
        try {
            Path path = Paths.get(fileDao.dir(), fileDao.id());
            Files.writeString(path, text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFromFile(FileDao fileDao) {
        try {
            Path path = Paths.get(fileDao.dir(), fileDao.id());
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
