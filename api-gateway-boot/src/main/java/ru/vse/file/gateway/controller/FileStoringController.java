package ru.vse.file.gateway.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.vse.utils.Asserts;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;
import ru.vse.file.gateway.exception.InternalServiceException;

@RestController
@RequestMapping("/file")
public class FileStoringController {
    private final RestTemplate restClient;
    private final String proxyEndpoint;

    public FileStoringController(RestTemplate restTemplate, String proxyEndpoint) {
        this.restClient = restTemplate;
        this.proxyEndpoint = Asserts.notEmpty(proxyEndpoint, "proxyEndpoint");
    }

    @ResponseBody
    @PostMapping(value = "/save", produces = "application/json")
    public IdDto save(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Сохранение текстового файла",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "text": "Здесь может быть любой текст для анализа. Текст не должен быть пустым. Слова текст и быть выделены.",
                                "filename": "sample-text.txt"
                            }
                            """)
                    )
            )
            FileDto fileDto) {
        var url = proxyEndpoint + "/file/save";
        try {
            return restClient.postForObject(url, fileDto, IdDto.class);
        } catch (Exception e) {
            throw new InternalServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping(value = "/find", produces = "application/json")
    public FileDto find(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Получение содержимого файла по его идентификатору"
            )
            IdDto idDto) {
        var url = proxyEndpoint + "/file/find";
        try {
            return restClient.postForObject(url, idDto, FileDto.class);
        } catch (Exception e) {
            throw new InternalServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
