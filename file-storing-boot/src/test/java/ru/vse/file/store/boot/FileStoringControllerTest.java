package ru.vse.file.store.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import ru.vse.file.dto.ErrorResponse;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FileStoringControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Value("${file-dir}")
    String fileDir;

    @BeforeEach
    public void beforeEach() throws IOException {
        Files.createDirectories(Path.of(fileDir));
    }

    @Test
    public void should_store_then_find_file() {
        var expected = new FileDto()
                .setFilename(UUID.randomUUID().toString())
                .setText(UUID.randomUUID().toString());

        var idDto = testRestTemplate.postForObject("/file/save", expected, IdDto.class);
        assertNotNull(idDto);
        var actual = testRestTemplate.postForObject("/file/find", idDto, FileDto.class);

        assertNotNull(actual);
        assertEquals(expected.getFilename(), actual.getFilename());
        assertEquals(expected.getText(), actual.getText());
    }

    @Test
    public void should_return_BAD_REQUEST_when_validation_failed() {
        var idDto = new IdDto()
                .setValue("123_XYZ");

        var res = testRestTemplate.postForEntity("/file/find", idDto, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getBody().getStatus().value());
        assertNotNull(res.getBody().getMessage());
    }
}
