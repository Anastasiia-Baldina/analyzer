package ru.vse.file.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

@Schema(description = "Статистика по файлу")
public class StatisticsDto {
    @NotNull(message = "File id mustn't be null")
    @Schema(description = "Идентификатор файла")
    private IdDto fileId;
    @PositiveOrZero(message = "Word count must be greater then zero.")
    @Schema(description = "Количество слов в тексте")
    private int wordCount;
    @PositiveOrZero(message = "Text length must be greater then zero.")
    @Schema(description = "Количество символов в тексте")
    private int textLength;
    @PositiveOrZero(message = "Paragraph count must be greater then zero.")
    @Schema(description = "Количество абзацев в тексте")
    private int paragraphCount;
    @NotNull
    @Schema(description = "Идентификаторы файлов имеющие такую же хэш сумму (плагиат)")
    @ArraySchema(
            schema = @Schema(implementation = IdDto.class),
            uniqueItems = true)
    private List<IdDto> hashClones;

    public IdDto getFileId() {
        return fileId;
    }

    public StatisticsDto setFileId(IdDto fileId) {
        this.fileId = fileId;
        return this;
    }

    public int getWordCount() {
        return wordCount;
    }

    public StatisticsDto setWordCount(int wordCount) {
        this.wordCount = wordCount;
        return this;
    }

    public int getTextLength() {
        return textLength;
    }

    public StatisticsDto setTextLength(int textLength) {
        this.textLength = textLength;
        return this;
    }

    public int getParagraphCount() {
        return paragraphCount;
    }

    public StatisticsDto setParagraphCount(int paragraphCount) {
        this.paragraphCount = paragraphCount;
        return this;
    }

    public List<IdDto> getHashClones() {
        return hashClones;
    }

    public StatisticsDto setHashClones(List<IdDto> hashClones) {
        this.hashClones = hashClones;
        return this;
    }
}
