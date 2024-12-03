package dev.fabled.astra.utils;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ListUtils {

    public static @NotNull List<String> onlinePlayerNames() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

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

    public static @Nullable List<String> fromConfig(final @NotNull YamlConfig config, final @NotNull String key) {
        return fromConfig(config, key, (List<String>) null);
    }

    @Contract("_, _, _, !null -> !null")
    public static @Nullable List<String> fromConfig(
            final @NotNull YamlConfig config,
            final @NotNull String key,
            final @NotNull String delimiter,
            final @Nullable List<String> def
    ) {
        final List<String> list = fromConfig(config, key, (List<String>) null);
        if (list == null) {
            return def;
        }

        final List<String> separated = new ArrayList<>();
        list.forEach(string -> {
            final String[] split = string.split(delimiter);
            Collections.addAll(separated, split);
        });

        return separated;
    }

    public static @Nullable List<String> fromConfig(
            final @NotNull YamlConfig config,
            final @NotNull String key,
            final @NotNull String delimiter
    ) {
        return fromConfig(config, key, delimiter, null);
    }

    public static @NotNull List<String> replace(final @NotNull List<String> list, final @NotNull List<String> replacements) {
        int size = replacements.size();
        if (size % 2 != 0) {
            size--;
        }

        if (size <= 0) {
            return list;
        }

        final List<String> parsed = new ArrayList<>();
        for (String s : list) {
            for (int i = 0; i < size; i+=2) {
                s = s.replace(replacements.get(i), replacements.get(i+1));
            }

            parsed.add(s);
        }

        return parsed;
    }

    public static @NotNull List<String> replace(final @NotNull List<String> list, final @NotNull String... replacements) {
        return replace(list, List.of(replacements));
    }

}
