package ru.vse.file.analysis.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FileAnalysisBoot {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FileAnalysisBoot.class)
                .run(args);
    }
}
