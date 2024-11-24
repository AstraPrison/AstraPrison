package dev.fabled.astra.utils;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {

    @Contract("_, _, !null -> !null")
    public static @Nullable List<String> fromConfig(
            final @NotNull YamlConfig config,
            final @NotNull String key,
            final @Nullable List<String> def
    ) {
        final Object o = config.options().get(key);
        if (o == null) {
            return def;
        }

        if (o instanceof List<?>) {
            return config.options().getStringList(key);
        }

        return new ArrayList<>(List.of(o.toString()));
    }

    public static @Nullable List<String> fromConfig(final YamlConfig config, final @NotNull String key) {
        return fromConfig(config, key, null);
    }

}
