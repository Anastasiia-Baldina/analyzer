package ru.vse.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Сохраняемый файл")
public class FileDto {
    @NotBlank(message = "Filename mustn't be blank")
    @Schema(description = "Имя файла")
    private String filename;
    @NotEmpty(message = "Text mustn't be empty")
    @Schema(description = "Текст, содержащийся в файле")
    private String text;

    public String getFilename() {
        return filename;
    }

    public FileDto setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getText() {
        return text;
    }

    public FileDto setText(String text) {
        this.text = text;
        return this;
    }
}
