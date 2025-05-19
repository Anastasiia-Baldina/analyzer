package ru.vse.file.analysis.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.vse.file.dto.ErrorResponse;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;
import ru.vse.file.dto.StatisticsDto;
import ru.vse.image.dto.WordCloudRequest;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FileAnalysisControllerTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Value("${remote-endpoints.file-storing-service}")
    String storingEndpoint;
    @Value("${remote-endpoints.word-cloud-service}")
    String cloudEndpoint;
    @Value("${picture-dir}")
    String pictureDir;
    MockRestServiceServer mockServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        Files.createDirectories(Path.of(pictureDir));
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void validate_statistics_calculation() throws Exception {
        var idDto = new IdDto()
                .setValue(UUID.randomUUID().toString());
        var fileDto = new FileDto()
                .setFilename(UUID.randomUUID().toString())
                .setText(textForCalculation());
        var fileUri = new URI(storingEndpoint + "/file/find");
        mockServer.expect(ExpectedCount.once(), requestTo(fileUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(idDto)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(fileDto)));
        var imageUri = new URI(cloudEndpoint + "/word-cloud-api/generate");
        var imageDto = new WordCloudRequest()
                .setHeight(600)
                .setWidth(800)
                .setText(fileDto.getText());
        byte[] image = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        mockServer.expect(ExpectedCount.once(), requestTo(imageUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(imageDto)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.IMAGE_PNG)
                                .body(image));

        var res = testRestTemplate.postForEntity("/analysis/analyze", idDto, StatisticsDto.class);

        mockServer.verify();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(9, res.getBody().getWordCount());
        assertEquals(2, res.getBody().getParagraphCount());
        assertEquals(45, res.getBody().getTextLength());
    }

    @Test
    public void should_return_BAD_REQUEST_when_validation_failed() {
        var idDto = new IdDto()
                .setValue("123_XYZ");

        var res = testRestTemplate.postForEntity("/analysis/analyze", idDto, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getBody().getStatus().value());
        assertNotNull(res.getBody().getMessage());
    }

    @Test
    public void should_return_word_cloud_image() throws Exception {
        var idDto = new IdDto()
                .setValue(UUID.randomUUID().toString());
        var fileDto = new FileDto()
                .setFilename(UUID.randomUUID().toString())
                .setText(UUID.randomUUID().toString());
        var fileUri = new URI(storingEndpoint + "/file/find");
        mockServer.expect(ExpectedCount.once(), requestTo(fileUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(idDto)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(fileDto)));
        var imageUri = new URI(cloudEndpoint + "/word-cloud-api/generate");
        var imageDto = new WordCloudRequest()
                .setHeight(600)
                .setWidth(800)
                .setText(fileDto.getText());
        byte[] image = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        mockServer.expect(ExpectedCount.once(), requestTo(imageUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(imageDto)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.IMAGE_PNG)
                                .body(image));

        testRestTemplate.postForEntity("/analysis/analyze", idDto, StatisticsDto.class);
        var res = testRestTemplate.postForEntity("/analysis/word-cloud-image", idDto, byte[].class);

        mockServer.verify();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(new String(image), new String(res.getBody()));
    }

    private static String textForCalculation() {
        return """
                В этом тексте девять слов.
                И еще два абзаца.
                """;
    }
}
