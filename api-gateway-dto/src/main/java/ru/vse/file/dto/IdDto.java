package ru.vse.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Идентификатор файла")
public class IdDto {
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Id value must be in uuid format")
    @Schema(description = "Значение идентификатора в формате UUID")
    private String value;

    public String getValue() {
        return value;
    }

    public IdDto setValue(String value) {
        this.value = value;
        return this;
    }
}
