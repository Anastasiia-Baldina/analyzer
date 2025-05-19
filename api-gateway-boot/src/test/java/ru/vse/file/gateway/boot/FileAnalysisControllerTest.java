package ru.vse.file.gateway.boot;

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
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;
import ru.vse.file.dto.StatisticsDto;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileAnalysisControllerTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Value("${proxy-endpoints.file-analysis}")
    String remoteEndpoint;
    MockRestServiceServer mockServer;

    @BeforeEach
    public void beforeEach() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void should_proxy_analyze_request_then_return_StatisticsDto_response() throws Exception {
        var uri = new URI(remoteEndpoint + "/analysis/analyze");
        var idDto = new IdDto()
                .setValue(UUID.randomUUID().toString());
        var cloneDto = new IdDto()
                .setValue(UUID.randomUUID().toString());
        var statDto = new StatisticsDto()
                .setFileId(idDto)
                .setHashClones(List.of(cloneDto))
                .setParagraphCount(10)
                .setTextLength(100)
                .setWordCount(20);

        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(idDto)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(statDto)));

        var res = testRestTemplate.postForEntity("/analysis/analyze", idDto, StatisticsDto.class);

        mockServer.verify();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(idDto.getValue(), res.getBody().getFileId().getValue());
        assertEquals(statDto.getParagraphCount(), res.getBody().getParagraphCount());
        assertEquals(statDto.getWordCount(), res.getBody().getWordCount());
        assertEquals(statDto.getTextLength(), res.getBody().getTextLength());
        assertEquals(1, res.getBody().getHashClones().size());
        assertEquals(cloneDto.getValue(), res.getBody().getHashClones().getFirst().getValue());
    }

    @Test
    public void should_proxy_image_request() throws Exception {
        var uri = new URI(remoteEndpoint + "/analysis/word-cloud-image");
        var idDto = new IdDto()
                .setValue(UUID.randomUUID().toString());
        byte[] image = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(idDto)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.IMAGE_PNG)
                                .body(image));

        var res = testRestTemplate.postForEntity("/analysis/word-cloud-image", idDto, byte[].class);

        mockServer.verify();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(new String(image), new String(res.getBody()));
    }
}
