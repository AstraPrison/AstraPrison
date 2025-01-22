package dev.fabled.astra.utils.configuration;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.JsonUtils;
import dev.fabled.astra.utils.logger.AstraLog;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public final class JsonConfig implements AstraConfig {

    private final @NotNull String filePath;
    private final @NotNull JsonConfiguration jsonConfiguration;
    private File file;

    public JsonConfig(final @NotNull String filePath) {
        this.filePath = filePath.endsWith(".json") ? filePath : filePath + ".json";
        jsonConfiguration = new JsonConfiguration();
        save();
        reload();
    }

    private File file() {
        return new File(Astra.getPlugin().getDataFolder(), filePath);
    }

    public @NotNull JsonConfiguration options() {
        return jsonConfiguration;
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
        try {
            jsonConfiguration.setMap(JsonUtils.toMap(
                    Files.readString(file().toPath(), StandardCharsets.UTF_8))
            );
        }
        catch (IOException e) {
            AstraLog.log(e);
        }
    }

    public void saveChanges(final boolean truncate) {
        final String jsonString = JsonUtils.toJsonString(jsonConfiguration.getMap());

        //noinspection ResultOfMethodCallIgnored
        file.delete();

        try {
            if (truncate) {
                Files.write(file.toPath(), jsonString.getBytes());
                return;
            }

            Files.write(
                    file.toPath(),
                    jsonString.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
            );
        }
        catch (IOException e) {
            AstraLog.log(e);
        }
    }

    @Override
    public void saveChanges() {
        saveChanges(false);
    }

}
