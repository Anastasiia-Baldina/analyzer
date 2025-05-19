package ru.vse.file.store.boot.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.vse.file.store.controller.ErrorHandlerController;
import ru.vse.file.store.controller.FileStoringController;
import ru.vse.file.store.properties.DbProperties;
import ru.vse.file.store.repository.FileStoringRepository;
import ru.vse.file.store.repository.postgres.PostgresFileStoringRepository;
import ru.vse.file.store.service.FileStoringService;
import ru.vse.file.store.service.FileStoringServiceImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class ApplicationConfiguration {
    @Value("${file-dir}")
    String fileDir;
    @Autowired
    DataSource dataSource;

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    FileStoringRepository fileStoringRepository() {
        return new PostgresFileStoringRepository(namedParameterJdbcTemplate());
    }

    @Bean
    FileStoringService fileStoringService() {
        return new FileStoringServiceImpl(fileStoringRepository(), fileDir);
    }

    @Bean
    FileStoringController fileStoringController() {
        return new FileStoringController(fileStoringService());
    }

    @Bean
    ErrorHandlerController errorHandlerController() {
        return new ErrorHandlerController();
    }
}
