package org.vse.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Asserts {
    private Asserts() {
    }

    @NotNull
    public static <T> T notNull(@Nullable T param, String paramName) {
        if(param == null) {
            throw new IllegalArgumentException("Parameter ["+ paramName + "] mustn't be null");
        }
        return param;
    }

    public static String notEmpty(String param, String paramName) {
        if(notNull(param, paramName).isEmpty()) {
            throw new IllegalArgumentException("Parameter ["+ paramName + "] mustn't be empty");
        }
        return param;
    }
}
