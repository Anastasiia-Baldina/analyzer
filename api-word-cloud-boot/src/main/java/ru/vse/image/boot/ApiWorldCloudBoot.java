package ru.vse.image.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ApiWorldCloudBoot {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiWorldCloudBoot.class)
                .run(args);
    }
}
