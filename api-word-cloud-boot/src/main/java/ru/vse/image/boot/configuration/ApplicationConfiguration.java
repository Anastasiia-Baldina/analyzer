package ru.vse.image.boot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vse.image.controller.WordCloudController;
import ru.vse.image.service.WordCloudService;
import ru.vse.image.service.WordCloudServiceImpl;

@Configuration
public class ApplicationConfiguration {

    @Bean
    WordCloudService wordCloudService() {
        return new WordCloudServiceImpl();
    }

    @Bean
    WordCloudController wordCloudController() {
        return new WordCloudController(wordCloudService());
    }
}
