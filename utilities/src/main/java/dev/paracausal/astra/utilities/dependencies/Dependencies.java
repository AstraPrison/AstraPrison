package dev.paracausal.astra.utilities.dependencies;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;

public final class Dependencies {

    public static final boolean HAS_PLACEHOLDER_API;
    public static final boolean HAS_HEAD_DATABASE;
    public static final HeadDatabaseAPI HEAD_DATABASE_API;

    static {
        HAS_PLACEHOLDER_API = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        HAS_HEAD_DATABASE = Bukkit.getPluginManager().getPlugin("HeadDatabase") != null;

        if (HAS_HEAD_DATABASE) {
            HEAD_DATABASE_API = new HeadDatabaseAPI();
        }
        else {
            HEAD_DATABASE_API = null;
        }
    }

}
