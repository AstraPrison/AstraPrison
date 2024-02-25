package dev.paracausal.astra.utilities;

import dev.paracausal.astra.utilities.configuration.YamlConfig;
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

        return new ArrayList<>(Collections.singleton(object.toString()));
    }

    public static @NotNull List<String> fromConfig(@NotNull final YamlConfig config, @NotNull final String path) {
        List<String> list = fromConfig(config, path, new ArrayList<>());
        if (list == null) {
            list = new ArrayList<>();
        }

        return list;
    }

}
