package ru.vse.file.analysis.dao;

import org.vse.utils.Asserts;

public class StatDao {
    private final String fileId;
    private final int wordCount;
    private final int textLength;
    private final int paragraphCount;
    private final String picturePath;
    private final String fileHash;

    private StatDao(Builder b) {
        fileId = Asserts.notEmpty(b.fileId, "fileId");
        picturePath = Asserts.notEmpty(b.picturePath, "picturePath");
        wordCount = b.wordCount;
        textLength = b.textLength;
        paragraphCount = b.paragraphCount;
        fileHash = Asserts.notEmpty(b.fileHash, "fileHash");
    }

    public static Builder builder() {
        return new Builder();
    }

    public String fileId() {
        return fileId;
    }

    public int wordCount() {
        return wordCount;
    }

    public int textLength() {
        return textLength;
    }

    public int paragraphCount() {
        return paragraphCount;
    }

    public String picturePath() {
        return picturePath;
    }

    public String fileHash() {
        return fileHash;
    }

    public static class Builder {
        private String fileId;
        private int wordCount;
        private int textLength;
        private int paragraphCount;
        private String picturePath;
        private String fileHash;

        public StatDao build() {
            return new StatDao(this);
        }

        public Builder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setWordCount(int wordCount) {
            this.wordCount = wordCount;
            return this;
        }

        public Builder setTextLength(int textLength) {
            this.textLength = textLength;
            return this;
        }

        public Builder setParagraphCount(int paragraphCount) {
            this.paragraphCount = paragraphCount;
            return this;
        }

        public Builder setPicturePath(String picturePath) {
            this.picturePath = picturePath;
            return this;
        }

        public Builder setFileHash(String fileHash) {
            this.fileHash = fileHash;
            return this;
        }
    }
}
