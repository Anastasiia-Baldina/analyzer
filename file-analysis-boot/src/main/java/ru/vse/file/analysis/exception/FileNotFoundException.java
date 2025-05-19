package ru.vse.file.analysis.exception;

import org.springframework.http.HttpStatus;

public class FileNotFoundException extends InternalServiceException {

    public FileNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, "Файл с идентификатором '%s' не найден".formatted(id));
    }
}
