package dev.fabled.astra.utils.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JsonConfig {

    private @NotNull Map<String, Object> map;

    public JsonConfig(String s) {
        map = new HashMap<>();
    }

    @NotNull Map<String, Object> getMap() {
        return map;
    }

    void setMap(@NotNull final Map<String, Object> map) {
        this.map = map;
    }

    public void set(@NotNull final String path, @Nullable final Object object) {
        if (object == null) {
            map.remove(path);
            return;
        }

        map.put(path, object);
    }

    public Object get(@NotNull final String path, @Nullable final Object def) {
        return map.getOrDefault(path, def);
    }


    public Object get(@NotNull final String path) {
        return get(path, null);
    }

    public void load(@NotNull File file) {
        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            map = gson.fromJson(reader, new HashMap<String, Object>().getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(@NotNull File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(map);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
