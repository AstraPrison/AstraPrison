package dev.fabled.astra;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Astra {

    private static JavaPlugin plugin;
    private static AstraUtilities utilities;

    static void onLoad(@NotNull final JavaPlugin plugin) {
        Astra.plugin = plugin;
        utilities = (AstraUtilities) plugin;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static AstraUtilities getUtilities() {
        return utilities;
    }

}
