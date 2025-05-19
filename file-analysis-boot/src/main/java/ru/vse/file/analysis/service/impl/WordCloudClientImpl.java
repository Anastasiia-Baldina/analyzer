package ru.vse.file.analysis.service.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.vse.file.analysis.exception.InternalServiceException;
import ru.vse.file.analysis.service.WordCloudClient;
import ru.vse.image.dto.WordCloudRequest;

public class WordCloudClientImpl implements WordCloudClient {
    private static final int PNG_WIDTH = 800;
    private static final int PNG_HEIGHT = 600;
    private final RestTemplate restClient;
    private final String worldCloudUrl;

    public WordCloudClientImpl(RestTemplate restClient, String worldCloudEndpoint) {
        this.restClient = restClient;
        this.worldCloudUrl = worldCloudEndpoint + "/word-cloud-api/generate";
    }

    @NotNull
    @Override
    public byte[] createPicture(String text) {
        ResponseEntity<byte[]> rsp = restClient.postForEntity(
                worldCloudUrl,
                new WordCloudRequest()
                        .setText(text)
                        .setWidth(PNG_WIDTH)
                        .setHeight(PNG_HEIGHT),
                byte[].class);
        var res =  rsp.getBody();
        if (rsp.getStatusCode().value() != HttpStatus.OK.value() || res == null || res.length == 0) {
            throw new InternalServiceException("Ошибка получения картинки с word-cloud-api");
        }
        return res;
    }
}
