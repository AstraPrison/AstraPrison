package dev.paracausal.astra.utilities.dependencies;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;

public final class Dependencies {

    public static final boolean HAS_PLACEHOLDER_API;

    static {
        HAS_PLACEHOLDER_API = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

}
