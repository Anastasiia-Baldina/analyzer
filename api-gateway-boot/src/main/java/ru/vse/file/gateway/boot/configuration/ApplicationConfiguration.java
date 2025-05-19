package ru.vse.file.gateway.boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import ru.vse.file.gateway.controller.ErrorHandlerController;
import ru.vse.file.gateway.controller.FileAnalysisController;
import ru.vse.file.gateway.controller.FileStoringController;
import ru.vse.file.gateway.service.ProxyResponseErrorHandler;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    @Value("${proxy-endpoints.file-storing}")
    String fileStoringEndpoint;
    @Value("${proxy-endpoints.file-analysis}")
    String fileAnalysisEndpoint;

    @Bean
    ResponseErrorHandler responseErrorHandler() {
        return new ProxyResponseErrorHandler();
    }

    @Bean
    RestTemplate restTemplate() {
        return restTemplateBuilder
                .errorHandler(responseErrorHandler())
                .build();
    }

    @Bean
    FileStoringController fileStoringController() {
        return new FileStoringController(restTemplate(), fileStoringEndpoint);
    }

    @Bean
    FileAnalysisController fileAnalysisController() {
        return new FileAnalysisController(restTemplate(), fileAnalysisEndpoint);
    }

    @Bean
    ErrorHandlerController errorHandlerController() {
        return new ErrorHandlerController();
    }
}
