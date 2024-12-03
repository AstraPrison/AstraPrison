package dev.fabled.astra.utils.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.logger.AstraLog;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public final class JsonConfig implements AstraConfig {

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
            AstraLog.log(e);
        }
    }

    @Override
    public void saveChanges() {
        final String json = GSON.toJson(
                convertToJsonObject(jsonConfiguration.getMap())
        );

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
            AstraLog.log(e);
        }
    }

    /**
     * Convert the map into a JsonObject
     * @param data The {@link Map} of our data
     * @return {@link JsonObject}
     * @author <a href='https://github.com/Loudbooks'>Loudbooks</a>
     */
    private @NotNull JsonObject convertToJsonObject(final @NotNull Map<String, Object> data) {
        final JsonObject jsonObject = new JsonObject();

        data.forEach((key, value) -> {
            JsonObject current = jsonObject;
            final String[] path = key.split("\\.");

            for (final String s : path) {
                if (!current.has(s)) {
                    current.add(s, new JsonObject());
                }

                current = current.getAsJsonObject(s);
            }

            current.addProperty(path[path.length - 1], value.toString());
        });

        return jsonObject;
    }

}
