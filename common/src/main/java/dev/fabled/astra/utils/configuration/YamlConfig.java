package dev.fabled.astra.utils.configuration;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.logger.AstraLog;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class YamlConfig implements AstraConfig {

    private final @NotNull String filePath;
    private File file;
    private FileConfiguration fileConfiguration;

    public YamlConfig(final @NotNull String filePath) {
        this.filePath = filePath.endsWith(".yml") ? filePath : filePath + ".yml";
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
    public void save() {
        if (file == null) {
            file = file();
        }

        if (file.exists()) {
            return;
        }

        final JavaPlugin plugin = Astra.getPlugin();
        if (plugin.getResource(filePath) == null) {
            return;
        }

        plugin.saveResource(filePath, false);
    }

    @Override
    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file());
    }

    @Override
    public void saveChanges() {
        if (fileConfiguration == null || file == null) {
            return;
        }

        try {
            fileConfiguration.save(file);
        }
        catch (IOException e) {
            AstraLog.log(e);
        }
    }

}
