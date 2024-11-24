package dev.fabled.astra;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Astra {

    private static JavaPlugin plugin;

    static void onLoad(final @NotNull JavaPlugin plugin) {
        Astra.plugin = plugin;
    }

    public static AstraPlugin get() {
        return (AstraPlugin) plugin;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

}
