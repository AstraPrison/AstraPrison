package dev.fabled.astra.utils.configuration;

import dev.fabled.astra.Astra;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class YamlConfig implements AstraConfig {

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

    @Override
    public void saveFile() {
        if (fileConfiguration == null || file == null) {
            return;
        }

        try { fileConfiguration.save(file); }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        if (file == null) {
            file = file();
        }

        if (file.exists()) {
            return;
        }

        Astra.getPlugin().saveResource(filePath, false);
    }

    @Override
    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file());
    }

}
