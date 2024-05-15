package dev.fabled.astra.utils.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.fabled.astra.Astra;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class JsonConfig implements AstraConfig {

    private static final Gson gson;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    private @NotNull final String filePath;
    private File file;
    private final JsonConfiguration jsonConfiguration;

    public JsonConfig(@NotNull final String filePath) {
        this.filePath = filePath.endsWith(".json") ? filePath : filePath + ".json";
        jsonConfiguration = new JsonConfiguration();
        save();
        reload();
        save();
    }

    private File file() {
        return new File(Astra.getPlugin().getDataFolder(), filePath);
    }

    public JsonConfiguration options() {
        return jsonConfiguration;
    }

    @Override
    public void saveFile() {
        final String json = gson.toJson(jsonConfiguration.getMap());
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
        try {
            //noinspection unchecked
            jsonConfiguration.setMap(gson.fromJson(new FileReader(file), HashMap.class));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
