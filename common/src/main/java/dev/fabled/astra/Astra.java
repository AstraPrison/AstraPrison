package dev.fabled.astra;

import dev.fabled.astra.utils.configuration.AllowedMaterials;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Astra {

    private static JavaPlugin plugin;
    private static AstraUtilities utilities;

    private static AllowedMaterials allowedMaterials;

    static void onLoad(final JavaPlugin plugin) {
        Astra.plugin = plugin;
        utilities = (AstraUtilities) plugin;
        String filePath = "plugins/Astra/data/allowedmaterials.json";
        File file = new File(filePath);

        if (!file.exists()) {
            AllowedMaterials.createDefaultJsonFile(filePath);
        }
        allowedMaterials = AllowedMaterials.loadFromJson("plugins/Astra/data/allowedmaterials.json");
    }

    public static AllowedMaterials getAllowedMaterials() {
        return allowedMaterials;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static AstraUtilities getUtilities() {
        return utilities;
    }
}
