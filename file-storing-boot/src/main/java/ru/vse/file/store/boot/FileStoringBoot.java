package ru.vse.file.store.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FileStoringBoot {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FileStoringBoot.class)
                .run(args);
    }
}
