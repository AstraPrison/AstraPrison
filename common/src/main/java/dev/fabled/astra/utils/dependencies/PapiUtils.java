package dev.fabled.astra.utils.dependencies;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PapiUtils {

    private static boolean hasPlaceholderApi;

    public static void onLoad() {
        onReload();
    }

    public static void onReload() {
        hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static boolean hasPlaceholderApi() {
        return hasPlaceholderApi;
    }

    public static @NotNull String parse(final @NotNull String input, final @Nullable Player player) {
        if (player == null || !hasPlaceholderApi) {
            return input;
        }

        return PlaceholderAPI.setPlaceholders(player, input).replace("{PLAYER}", player.getName());
    }

    public static @NotNull List<String> parse(final @NotNull List<String> input, final @Nullable Player player) {
        if (player == null || !hasPlaceholderApi) {
            return input;
        }

        return PlaceholderAPI.setPlaceholders(player, input);
    }

}
