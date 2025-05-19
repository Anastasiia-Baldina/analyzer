package ru.vse.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на формирование картинки word-cloud")
public class WordCloudRequest {
    @NotBlank(message = "Текст не должен быть пустым")
    @Schema(description = "Текст по которому будет формироваться 'облако слов'")
    private String text;
    @Min(value = 300, message = "Минимальная ширина картинки 300px")
    @Max(value = 2000, message = "Максимальная ширина картинки 2000px")
    @Schema(description = "Ширина картинки в пикселях")
    private int width;
    @Min(value = 300, message = "Минимальная высота картинки 300px")
    @Max(value = 2000, message = "Максимальная высота картинки 2000px")
    @Schema(description = "Высота картинки в пикселях")
    private int height;

    public String getText() {
        return text;
    }

    public WordCloudRequest setText(String text) {
        this.text = text;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public WordCloudRequest setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public WordCloudRequest setHeight(int height) {
        this.height = height;
        return this;
    }
}
