package ru.vse.file.gateway.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.vse.file.dto.IdDto;
import ru.vse.file.dto.StatisticsDto;
import ru.vse.file.gateway.exception.InternalServiceException;

@RestController
@RequestMapping("/analysis")
public class FileAnalysisController {
    private final RestTemplate restClient;
    private final String proxyEndpoint;

    public FileAnalysisController(RestTemplate restClient, String proxyEndpoint) {
        this.restClient = restClient;
        this.proxyEndpoint = proxyEndpoint;
    }

    @ResponseBody
    @PostMapping(value = "/analyze", produces = "application/json")
    public StatisticsDto analyze(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Запуск анализа файла по его идентификатору"
            )
            IdDto idDto) {
        try {
            var url = proxyEndpoint + "/analysis/analyze";
            return restClient.postForObject(url, idDto, StatisticsDto.class);
        } catch (Exception e) {
            throw new InternalServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping(value = "/word-cloud-image", consumes = "application/json", produces = "image/png")
    public ResponseEntity<byte[]> wordCloudImage(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Получение картинки word-cloud по идентификатору файла. " +
                            "Картинка формируется при вызове метода 'analyze'."
            )
            IdDto idDto) {
        try {
            var url = proxyEndpoint + "/analysis/word-cloud-image";
            return restClient.postForEntity(url, idDto, byte[].class);
        } catch (Exception e) {
            throw new InternalServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
