package ru.vse.image.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import ru.vse.image.dto.WordCloudRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WordCloudControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void should_generate_image() {
        var rqDto = new WordCloudRequest()
                .setHeight(600)
                .setWidth(800)
                .setText(UUID.randomUUID().toString());

        var res = testRestTemplate.postForEntity("/word-cloud-api/generate", rqDto, byte[].class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
    }
}
