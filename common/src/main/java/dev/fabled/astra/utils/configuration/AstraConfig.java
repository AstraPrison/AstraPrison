package dev.fabled.astra.utils.configuration;
import org.bukkit.configuration.file.FileConfiguration;

public interface AstraConfig {

    /**
     * Saves the file to the server!
     */
    void save();

    /**
     * Loads the file from the server into the {@link FileConfiguration}!
     */
    void reload();

    /**
     * Saves the changes from the {@link FileConfiguration} to the file on the server!
     */
    void saveChanges();

}
