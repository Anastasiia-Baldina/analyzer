package ru.vse.file.analysis.service.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vse.file.analysis.dao.StatDao;
import ru.vse.file.analysis.exception.FileNotFoundException;
import ru.vse.file.analysis.exception.InternalServiceException;
import ru.vse.file.analysis.repository.StatRepository;
import ru.vse.file.analysis.service.FileAnalysisService;
import ru.vse.file.analysis.service.FileStoreClient;
import ru.vse.file.analysis.service.HashCalculator;
import ru.vse.file.analysis.service.TextStatCalculator;
import ru.vse.file.analysis.service.WordCloudClient;
import ru.vse.file.dto.IdDto;
import ru.vse.file.dto.StatisticsDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class FileAnalysisServiceImpl implements FileAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(FileAnalysisServiceImpl.class);
    private final StatRepository statRepository;
    private final TextStatCalculator statCalculator;
    private final WordCloudClient worldCloudClient;
    private final FileStoreClient fileStoreClient;
    private final HashCalculator hashCalculator;
    private final String pictureDir;

    public FileAnalysisServiceImpl(StatRepository statRepository,
                                   TextStatCalculator statCalculator,
                                   WordCloudClient worldCloudClient,
                                   FileStoreClient fileStoreClient,
                                   HashCalculator hashCalculator,
                                   String pictureDir) {
        this.statRepository = statRepository;
        this.statCalculator = statCalculator;
        this.worldCloudClient = worldCloudClient;
        this.fileStoreClient = fileStoreClient;
        this.hashCalculator = hashCalculator;
        this.pictureDir = pictureDir;
    }

    @Nullable
    @Override
    public StatisticsDto analyze(@NotNull IdDto idDto) {
        var fileId = idDto.getValue();
        var statDao = statRepository.findById(fileId);
        if (statDao == null) {
            var fileDto = fileStoreClient.findById(idDto);
            var fileText = fileDto.getText();
            var picturePath = Path.of(pictureDir, fileId + ".png");
            createWordCloudImage(picturePath, fileText);
            var textStat = statCalculator.calculate(fileText);
            statDao = StatDao.builder()
                    .setFileId(fileId)
                    .setWordCount(textStat.wordCount())
                    .setTextLength(textStat.length())
                    .setParagraphCount(textStat.paragraphCount())
                    .setPicturePath(picturePath.toString())
                    .setFileHash(hashCalculator.calculate(fileText))
                    .build();
            statRepository.insert(statDao);
        }
        List<String> cloneIds = statRepository.findByHash(statDao.fileHash()).stream()
                .filter(eachId -> !Objects.equals(fileId, eachId))
                .toList();
        return toDto(statDao, cloneIds);
    }

    @NotNull
    @Override
    public byte[] getWordCloudImage(@NotNull IdDto idDto) {
        var fileId = idDto.getValue();
        var statDao = statRepository.findById(fileId);
        if (statDao == null) {
            throw new FileNotFoundException(
                    "Word-cloud image not found for id=%s. Please analyze before."
                            .formatted(idDto.getValue()));
        }
        var filePath = statDao.picturePath();
        try {
            return Files.readAllBytes(Path.of(filePath));
        } catch (IOException e) {
            log.error("File read error. path={}", filePath, e);
            throw new InternalServiceException("Word-cloud image read error.");
        }
    }

    private void createWordCloudImage(Path path, String text) {
        try {
            Files.write(path, worldCloudClient.createPicture(text));
        } catch (IOException e) {
            throw new InternalServiceException("Ошибка записи word-cloud файла: " + e.getMessage());
        }
    }

    private static StatisticsDto toDto(StatDao statDao, List<String> cloneIds) {
        List<IdDto> hashClones = cloneIds.stream()
                .map(id -> new IdDto().setValue(id))
                .toList();
        return new StatisticsDto()
                .setFileId(new IdDto().setValue(statDao.fileId()))
                .setWordCount(statDao.wordCount())
                .setTextLength(statDao.textLength())
                .setParagraphCount(statDao.paragraphCount())
                .setHashClones(hashClones);
    }
}
