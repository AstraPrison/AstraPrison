package dev.fabled.astra.utils.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.fabled.astra.Astra;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class JsonConfig implements AstraConfig {

    private static final @NotNull Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

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
            //noinspection unchecked
            jsonConfiguration.setMap(GSON.fromJson(new FileReader(file), HashMap.class));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveChanges() {
        final String json = GSON.toJson(jsonConfiguration.getMap());

        //noinspection ResultOfMethodCallIgnored
        file.delete();

        try {
            Files.write(
                    file.toPath(),
                    json.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
