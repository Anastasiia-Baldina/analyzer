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
import ru.vse.file.dto.ErrorResponse;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileStoringControllerTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Value("${proxy-endpoints.file-storing}")
    String remoteEndpoint;
    MockRestServiceServer mockServer;

    @BeforeEach
    public void beforeEach() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void should_proxy_save_request_then_return_IdDto_response() throws Exception {
        var uri = new URI(remoteEndpoint + "/file/save");
        var fileDto = new FileDto()
                .setFilename(UUID.randomUUID().toString())
                .setText(UUID.randomUUID().toString());
        var idDto = new IdDto()
                .setValue(UUID.randomUUID().toString());
        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(fileDto)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(idDto)));

        var res = testRestTemplate.postForEntity("/file/save", fileDto, IdDto.class);

        mockServer.verify();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(idDto.getValue(), res.getBody().getValue());
    }

    @Test
    public void should_proxy_find_request_then_return_FileDto_response() throws Exception {
        var uri = new URI(remoteEndpoint + "/file/find");
        var fileDto = new FileDto()
                .setFilename(UUID.randomUUID().toString())
                .setText(UUID.randomUUID().toString());
        var idDto = new IdDto()
                .setValue(UUID.randomUUID().toString());
        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(idDto)))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(fileDto)));

        var res = testRestTemplate.postForEntity("/file/find", idDto, FileDto.class);

        mockServer.verify();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(fileDto.getFilename(), res.getBody().getFilename());
        assertEquals(fileDto.getText(), res.getBody().getText());
    }

    @Test
    public void should_return_ErrorResponse_when_exception_thrown() throws Exception {
        var uri = new URI(remoteEndpoint + "/file/find");
        var idDto = new IdDto()
                .setValue(UUID.randomUUID().toString());
        var errRsp = new ErrorResponse()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .setMessage(UUID.randomUUID().toString());
        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(idDto)))
                .andRespond(
                        withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(errRsp)));

        var res = testRestTemplate.postForEntity("/file/find", idDto, ErrorResponse.class);

        mockServer.verify();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(errRsp.getStatus().value(), res.getBody().getStatus().value());
        assertEquals(errRsp.getMessage(), res.getBody().getMessage());
    }

    @Test
    public void should_return_ErrorResponse_when_validation_failed() throws Exception {
        var idDto = new IdDto()
                .setValue("123_XYZ");

        var res = testRestTemplate.postForEntity("/file/find", idDto, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getBody().getStatus().value());
        assertNotNull(res.getBody().getMessage());
    }
}
