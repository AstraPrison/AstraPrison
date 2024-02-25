package dev.paracausal.astra.utilities.dependencies;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class PapiUtils {

    public static @NotNull String parse(@Nullable final OfflinePlayer player, @NotNull String input) {
        if (player == null) {
            return input;
        }

        String playerName = player.getName();
        if (playerName != null) {
            input = input.replace("{PLAYER}", playerName);
        }

        return PlaceholderAPI.setPlaceholders(player, input);
    }

    public static @NotNull List<String> parse(@Nullable final OfflinePlayer player, @NotNull final List<String> input) {
        if (player == null) {
            return input;
        }

        final String playerName = player.getName();
        if (playerName == null) {
            return PlaceholderAPI.setPlaceholders(player, input);
        }

        final List<String> toParse = new ArrayList<>();
        input.forEach(string -> toParse.add(string.replace("{PLAYER}", playerName)));
        return PlaceholderAPI.setPlaceholders(player, toParse);
    }

}
