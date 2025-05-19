package ru.vse.image.service;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class WordCloudServiceImpl implements WordCloudService {

    @Override
    public byte[] generate(String text, int width, int height) {
        try (var byteArrayStream = new ByteArrayOutputStream()){
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(
                    IOUtils.toInputStream(text, "UTF-8"));
            WordCloud wordCloud = initWordCloud(width, height);
            wordCloud.build(wordFrequencies);
            wordCloud.writeToStreamAsPNG(byteArrayStream);
            return byteArrayStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static WordCloud initWordCloud(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setBackgroundColor(new Color(0xFFFFFF));
        wordCloud.setColorPalette(
                new ColorPalette(
                        new Color(0x40D3F1),
                        new Color(0x40D3F1),
                        new Color(0x40C5F1),
                        new Color(0x40AAF1),
                        new Color(0x408DF1),
                        new Color(0x4055F1)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        return wordCloud;
    }
}
