package ru.vse.file.store.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;
import ru.vse.file.store.exception.FileNotFoundException;
import ru.vse.file.store.exception.InternalServiceException;
import ru.vse.file.store.service.FileStoringService;

@RestController
@RequestMapping("/file")
public class FileStoringController {
    private final FileStoringService fileStoringService;

    public FileStoringController(FileStoringService fileStoringService) {
        this.fileStoringService = fileStoringService;
    }

    @ResponseBody
    @PostMapping(value = "/save", produces = "application/json")
    public IdDto save(@RequestBody @Valid FileDto fileDto) {
        try {
            return fileStoringService.save(fileDto);
        } catch (Exception e) {
            throw new InternalServiceException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка на сервисе file-storing: %s".formatted(e.getMessage()));
        }
    }

    @ResponseBody
    @PostMapping(value = "/find", produces = "application/json")
    public FileDto find(@RequestBody @Valid IdDto idDto) {
        FileDto fileDto = null;
        try {
            fileDto = fileStoringService.get(idDto);
        } catch (Exception e) {
            throw new InternalServiceException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка на сервисе file-storing: %s".formatted(e.getMessage()));
        }
        if (fileDto == null) {
            throw new FileNotFoundException(idDto.getValue());
        }
        return fileDto;
    }
}
