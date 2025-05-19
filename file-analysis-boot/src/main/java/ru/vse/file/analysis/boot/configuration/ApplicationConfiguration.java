package ru.vse.file.analysis.boot.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;
import ru.vse.file.analysis.controller.ErrorHandlerController;
import ru.vse.file.analysis.controller.FileAnalysisController;
import ru.vse.file.analysis.properties.DbProperties;
import ru.vse.file.analysis.repository.StatRepository;
import ru.vse.file.analysis.repository.postgres.PostgresStatRepository;
import ru.vse.file.analysis.service.FileAnalysisService;
import ru.vse.file.analysis.service.FileStoreClient;
import ru.vse.file.analysis.service.HashCalculator;
import ru.vse.file.analysis.service.ProxyResponseErrorHandler;
import ru.vse.file.analysis.service.TextStatCalculator;
import ru.vse.file.analysis.service.WordCloudClient;
import ru.vse.file.analysis.service.impl.FileAnalysisServiceImpl;
import ru.vse.file.analysis.service.impl.FileStoreClientImpl;
import ru.vse.file.analysis.service.impl.HashCalculatorImpl;
import ru.vse.file.analysis.service.impl.TextStatCalculatorImpl;
import ru.vse.file.analysis.service.impl.WordCloudClientImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class ApplicationConfiguration {
    @Value("${hash-algorithm}")
    String hashAlgorithm;
    @Value("${picture-dir}")
    String pictureDir;
    @Value("${remote-endpoints.file-storing-service}")
    String fileStoringEndpoint;
    @Value("${remote-endpoints.word-cloud-service}")
    String wordCloudEndpoint;
    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    @Autowired
    DataSource dataSource;

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    StatRepository statRepository() {
        return new PostgresStatRepository(namedParameterJdbcTemplate());
    }

    @Bean
    ProxyResponseErrorHandler proxyResponseErrorHandler() {
        return new ProxyResponseErrorHandler();
    }

    @Bean
    RestTemplate restTemplate() {
        return restTemplateBuilder
                .errorHandler(proxyResponseErrorHandler())
                .build();
    }

    @Bean
    WordCloudClient wordCloudClient() {
        return new WordCloudClientImpl(restTemplate(), wordCloudEndpoint);
    }

    @Bean
    FileStoreClient fileStoreClient() {
        return new FileStoreClientImpl(restTemplate(), fileStoringEndpoint);
    }

    @Bean
    TextStatCalculator textStatCalculator() {
        return new TextStatCalculatorImpl();
    }


    @Bean
    HashCalculator hashCalculator() {
        return new HashCalculatorImpl(hashAlgorithm);
    }

    @Bean
    FileAnalysisService fileAnalysisService() {
        return new FileAnalysisServiceImpl(
                statRepository(),
                textStatCalculator(),
                wordCloudClient(),
                fileStoreClient(),
                hashCalculator(),
                pictureDir);
    }

    @Bean
    FileAnalysisController fileAnalysisController() {
        return new FileAnalysisController(fileAnalysisService());
    }

    @Bean
    ErrorHandlerController errorHandlerController() {
        return new ErrorHandlerController();
    }
}
