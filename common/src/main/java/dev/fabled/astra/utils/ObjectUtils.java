package dev.fabled.astra.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectUtils {

    public static <T> @NotNull T defaultValue(@Nullable final T value, @NotNull final T fallback) {
        return value == null ? fallback : value;
    }

}
