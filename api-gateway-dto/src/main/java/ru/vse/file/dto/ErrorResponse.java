package ru.vse.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

@Schema(description = "Описание ошибки")
public class ErrorResponse {
    @NotNull
    @Schema(description = "Http статус")
    private HttpStatus status;
    @NotNull
    @Schema(description = "Текст ошибки")
    private String message;

    public HttpStatus getStatus() {
        return status;
    }

    public ErrorResponse setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
