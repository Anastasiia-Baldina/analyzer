package ru.vse.file.analysis.service.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.client.RestTemplate;
import ru.vse.file.analysis.exception.FileNotFoundException;
import ru.vse.file.analysis.service.FileStoreClient;
import ru.vse.file.dto.FileDto;
import ru.vse.file.dto.IdDto;

public class FileStoreClientImpl implements FileStoreClient {
    private final RestTemplate restClient;
    private final String fileStoreUrl;

    public FileStoreClientImpl(RestTemplate restClient, String fileStoreEndpoint) {
        this.restClient = restClient;
        this.fileStoreUrl = fileStoreEndpoint + "/file/find";
    }

    @NotNull
    @Override
    public FileDto findById(IdDto idDto) {
        var fileDto = restClient.postForObject(fileStoreUrl, idDto, FileDto.class);
        if(fileDto == null) {
            throw new FileNotFoundException(idDto.getValue());
        }
        return fileDto;
    }
}
