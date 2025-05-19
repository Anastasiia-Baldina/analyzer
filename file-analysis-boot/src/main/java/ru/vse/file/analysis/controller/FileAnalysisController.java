package ru.vse.file.analysis.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vse.file.analysis.exception.InternalServiceException;
import ru.vse.file.analysis.service.FileAnalysisService;
import ru.vse.file.dto.IdDto;
import ru.vse.file.dto.StatisticsDto;

@RestController
@RequestMapping("/analysis")
public class FileAnalysisController {
    private final FileAnalysisService fileAnalysisService;

    public FileAnalysisController(FileAnalysisService fileAnalysisService) {
        this.fileAnalysisService = fileAnalysisService;
    }

    @ResponseBody
    @PostMapping(value = "/analyze", produces = "application/json")
    public StatisticsDto analyze(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody
            IdDto idDto) {
        try {
            return fileAnalysisService.analyze(idDto);
        } catch (InternalServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new InternalServiceException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка на сервисе file-analysis: %s".formatted(e.getMessage()));
        }
    }

    @ResponseBody
    @PostMapping(value = "/word-cloud-image", consumes = "application/json", produces = "image/png")
    public ResponseEntity<byte[]> wordCloudImage(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody
            IdDto idDto) {
        try {
            return ResponseEntity.ok(fileAnalysisService.getWordCloudImage(idDto));
        } catch (InternalServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new InternalServiceException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка на сервисе file-analysis: %s".formatted(e.getMessage()));
        }
    }
}
