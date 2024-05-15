package dev.fabled.astra.utils;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {

    public static @Nullable List<String> fromConfig(
            @NotNull final YamlConfig config,
            @NotNull final String path,
            @Nullable final List<String> def
    ) {
        final Object object = config.options().get(path);
        if (object == null) {
            return def;
        }

        if (object instanceof List<?>) {
            return config.options().getStringList(path);
        }

        return new ArrayList<>(Collections.singletonList(object.toString()));
    }

    public static @NotNull List<String> fromConfig(@NotNull final YamlConfig config, @NotNull final String path) {
        final List<String> result = fromConfig(config, path, null);
        return result == null ? new ArrayList<>() : result;
    }

}
