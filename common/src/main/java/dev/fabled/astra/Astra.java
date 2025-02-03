package dev.fabled.astra;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Astra {

    private static JavaPlugin plugin;
    private static NamespacedKey astraMenuItemKey;

    static void onLoad(final @NotNull JavaPlugin plugin) {
        Astra.plugin = plugin;
        astraMenuItemKey = new NamespacedKey(plugin, "astra-menu-item");
    }

    public static AstraPlugin get() {
        return (AstraPlugin) plugin;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static NamespacedKey getAstraMenuItemKey() {
        return astraMenuItemKey;
    }

}
