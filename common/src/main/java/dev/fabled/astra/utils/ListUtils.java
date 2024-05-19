package dev.fabled.astra.utils;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public static @Nullable List<String> fromConfigString(
            @NotNull final YamlConfig config,
            @NotNull final String path,
            @NotNull final String separator
    ) {
        final Object object = config.options().get(path);
        if (object == null) {
            return null;
        }

        if (object instanceof List<?>) {
            return config.options().getStringList(path);
        }

        return new ArrayList<>(
                Arrays.stream(object.toString().split(separator)).toList()
        );
    }

    public static @NotNull List<String> playerNames() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    public static @NotNull List<String> playerNames(@NotNull final CommandSender sender) {
        final List<String> names = playerNames();

        if (!(sender instanceof Player player)) {
            return names;
        }

        names.remove(player.getName());
        return names;
    }

    public static @NotNull List<String> replace(@NotNull final List<String> input, @Nullable final String... replacements) {
        if (replacements == null) {
            return input;
        }

        int length = replacements.length;
        if (length % 2 != 0) {
            length--;
        }

        if (length <= 0) {
            return input;
        }

        final List<String> parsed = new ArrayList<>();
        for (String string : input) {
            for (int i = 0; i < length; i += 2) {
                final String target = replacements[i];
                final String replacement = replacements[i + 1];

                if (target == null || replacement == null) {
                    continue;
                }

                string = string.replace(target, replacement);
            }

            parsed.add(string);
        }

        return parsed;
    }

}
