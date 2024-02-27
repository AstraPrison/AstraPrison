package dev.paracausal.astra;

import dev.paracausal.astra.api.AstraAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Astra {

    private static AstraUtility utility;
    private static NamespacedKey menuItemNamespacedKey;

    static void onLoad(@NotNull final AstraUtility plugin) {
        Astra.utility = plugin;
        menuItemNamespacedKey = new NamespacedKey((JavaPlugin) plugin, "astra-menu-item");
    }

    public static AstraUtility getUtility() {
        return utility;
    }

    public static JavaPlugin getPlugin() {
        return (JavaPlugin) utility;
    }

    public static AstraAPI getAPI() {
        return (AstraAPI) getPlugin();
    }

    public static NamespacedKey getMenuItemNamespacedKey() {
        return menuItemNamespacedKey;
    }

}
