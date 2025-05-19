package ru.vse.file.analysis.service.impl;

import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import ru.vse.file.analysis.service.HashCalculator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCalculatorImpl implements HashCalculator {
    private final String algorithm;

    public HashCalculatorImpl(String algorithm) {
        this.algorithm = algorithm;
    }

    @NotNull
    @Override
    public String calculate(String text) {
        try {
            var md = MessageDigest.getInstance(algorithm);
            var hash = md.digest(text.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
