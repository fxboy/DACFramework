package icu.weboys.dacf.core.util;

import org.springframework.lang.Nullable;

public class Assert{
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new RuntimeException(message);
        }
    }

    public static <T> T notNull(@Nullable T object, T message) {
        if (object == null) {
           return message;
        }
        return object;
    }
}
