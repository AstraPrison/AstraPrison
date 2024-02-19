package dev.paracausal.astra.utilities.dependencies;

import org.bukkit.Bukkit;

public final class Dependencies {

    public static final boolean hasPlaceholderAPI;

    static {
        hasPlaceholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

}
