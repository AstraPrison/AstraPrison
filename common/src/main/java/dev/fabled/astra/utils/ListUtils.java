package dev.fabled.astra.utils;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

}
