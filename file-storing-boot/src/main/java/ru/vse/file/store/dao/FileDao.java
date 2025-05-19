package ru.vse.file.store.dao;

import org.jetbrains.annotations.NotNull;
import org.vse.utils.Asserts;

public class FileDao {
    @NotNull
    private final String id;
    @NotNull
    private final String name;
    @NotNull
    private final String dir;

    private FileDao(Builder b) {
        this.id = Asserts.notEmpty(b.id, "file id");
        this.name = Asserts.notEmpty(b.name, "filename");
        this.dir = Asserts.notNull(b.dir, "file directory");
    }

    @NotNull
    public String id() {
        return id;
    }

    @NotNull
    public String name() {
        return name;
    }

    @NotNull
    public String dir() {
        return dir;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String dir;

        public FileDao build() {
            return new FileDao(this);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDir(String dir) {
            this.dir = dir;
            return this;
        }
    }
}
