package ru.vse.file.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import ru.vse.file.dto.ErrorResponse;
import ru.vse.file.gateway.exception.InternalServiceException;

import java.io.IOException;
import java.net.URI;

public class ProxyResponseErrorHandler implements ResponseErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ProxyResponseErrorHandler.class);
    private static final ObjectReader JSON_READER = new ObjectMapper()
            .readerFor(ErrorResponse.class);

    @Override
    public boolean hasError(@NotNull ClientHttpResponse response) throws IOException {
        return response.getStatusCode().value() != HttpStatus.OK.value();
    }

    @Override
    public void handleError(@NotNull URI url,
                            @NotNull HttpMethod method,
                            @NotNull ClientHttpResponse response) throws IOException {
            ErrorResponse errRsp = null;
            try {
                errRsp = JSON_READER.readValue(response.getBody());
            } catch (Exception e) {
                log.warn("Response doesn't contains ErrorResponse object. Url {}, status={}",
                        url, response.getStatusCode());
            }
            if(errRsp != null) {
                throw new InternalServiceException(errRsp.getStatus(), errRsp.getMessage());
            } else {
                throw new InternalServiceException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при вызове %s".formatted(url));
            }
    }
}
