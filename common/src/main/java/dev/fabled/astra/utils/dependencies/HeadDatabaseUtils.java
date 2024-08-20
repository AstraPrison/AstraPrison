package dev.fabled.astra.utils.dependencies;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class HeadDatabaseUtils {

    public static boolean hasHeadDatabase() {
        return Bukkit.getPluginManager().getPlugin("HeadDatabase") != null;
    }

    private static @Nullable HeadDatabaseAPI headDatabaseAPI;

    public static @Nullable HeadDatabaseAPI getHeadDatabaseAPI() {
        final boolean hasPlugin = hasHeadDatabase();
        if (!hasPlugin) {
            headDatabaseAPI = null;
        }

        else if (headDatabaseAPI == null) {
            headDatabaseAPI = new HeadDatabaseAPI();
        }

        return headDatabaseAPI;
    }

}
