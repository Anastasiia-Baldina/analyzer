package ru.vse.image.service;

public interface WordCloudService {
    byte[] generate(String text, int width, int height);
}
