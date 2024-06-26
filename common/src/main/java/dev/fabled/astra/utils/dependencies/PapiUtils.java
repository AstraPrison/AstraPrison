package dev.fabled.astra.utils.dependencies;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PapiUtils {

    public static boolean hasPlaceholderApi() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static @NotNull String parse(@Nullable final OfflinePlayer player, @NotNull final String input) {
        if (player == null || !hasPlaceholderApi()) {
            return input;
        }

        return PlaceholderAPI.setPlaceholders(player, input);
    }

    public static @NotNull List<String> parse(@Nullable final OfflinePlayer player, @NotNull final List<String> input) {
        if (player == null || !hasPlaceholderApi()) {
            return input;
        }

        return PlaceholderAPI.setPlaceholders(player, input);
    }

}
