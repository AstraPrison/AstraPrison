package dev.fabled.astra.utils.dependencies;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class HdbUtils {

    private static boolean hasHeadDatabase;
    private static HeadDatabaseAPI api;

    public static void onLoad() {
        onReload();
    }

    public static void onReload() {
        hasHeadDatabase = Bukkit.getPluginManager().getPlugin("HeadDatabase") != null;

        if (!hasHeadDatabase) {
            api = null;
            return;
        }

        api = new HeadDatabaseAPI();
    }

    public static boolean hasHeadDatabase() {
        return hasHeadDatabase;
    }

    public static @Nullable HeadDatabaseAPI getApi() {
        return api;
    }

}
