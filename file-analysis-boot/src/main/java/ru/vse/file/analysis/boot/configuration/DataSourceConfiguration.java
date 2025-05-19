package ru.vse.file.analysis.boot.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.vse.file.analysis.properties.DbProperties;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@Profile("!test")
public class DataSourceConfiguration {

    @ConfigurationProperties(prefix = "db")
    @Bean
    DbProperties dbProperties() {
        return new DbProperties();
    }

    @Bean
    @Profile("!test")
    public DataSource dataSource() {
        try {
            var cfg = dbProperties();
            var dSrc = new HikariDataSource();
            dSrc.setJdbcUrl(cfg.getUrl());
            dSrc.setUsername(cfg.getUser());
            dSrc.setPassword(cfg.getPassword());
            dSrc.setSchema(cfg.getSchema());
            dSrc.setMaximumPoolSize(cfg.getMaxPoolSize());

            dSrc.getConnection().isValid(5_000);
            return dSrc;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
