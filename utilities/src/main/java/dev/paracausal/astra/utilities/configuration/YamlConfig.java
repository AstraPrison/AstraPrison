package dev.paracausal.astra.utilities.configuration;

import dev.paracausal.astra.Astra;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class YamlConfig {

    private @NotNull final String filePath;
    private File file;
    private FileConfiguration fileConfiguration;

    public YamlConfig(@NotNull final String filePath) {
        this.filePath = filePath.endsWith(".yml") ? filePath : filePath + ".yml";
        reload();
        save();
        reload();
    }

    private File file() {
        return new File(Astra.getPlugin().getDataFolder(), filePath);
    }

    public FileConfiguration options() {
        return fileConfiguration;
    }

    public void saveFile() {
        if (fileConfiguration == null || file == null) {
            return;
        }

        try { fileConfiguration.save(file); }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (file == null) {
            file = file();
        }

        if (file.exists()) {
            return;
        }

        Astra.getPlugin().saveResource(filePath, false);
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file());
    }

}
