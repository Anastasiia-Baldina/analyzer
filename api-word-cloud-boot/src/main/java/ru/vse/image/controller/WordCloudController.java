package ru.vse.image.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vse.image.dto.WordCloudRequest;
import ru.vse.image.service.WordCloudService;

@RestController
@RequestMapping("/word-cloud-api")
public class WordCloudController {
    private final WordCloudService wordCloudService;

    public WordCloudController(WordCloudService wordCloudService) {
        this.wordCloudService = wordCloudService;
    }

    @ResponseBody
    @PostMapping(value = "/generate", consumes = "application/json", produces = "image/png")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "image/png",
                    schema = @Schema(
                            type = "string",
                            format = "binary")
            )
    )
    public ResponseEntity<byte[]> generate(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Формирование картинки word-cloud по переданному тексту",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "text": "Здесь может быть любой текст для анализа. Текст не должен быть пустым. Слова текст и быть выделены.",
                                "width": 400,
                                "height": 400
                            }
                            """)
                    )
            )
            WordCloudRequest wordCloudRequest) {

        return ResponseEntity.ok(
                wordCloudService.generate(
                        wordCloudRequest.getText(),
                        wordCloudRequest.getWidth(),
                        wordCloudRequest.getHeight()));
    }
}
