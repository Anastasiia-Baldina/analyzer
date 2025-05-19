package ru.vse.file.gateway.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ApiGatewayBoot {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiGatewayBoot.class)
                .run(args);
    }
}
