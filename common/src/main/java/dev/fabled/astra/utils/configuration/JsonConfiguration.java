package dev.fabled.astra.utils.configuration;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Map;

public class JsonConfiguration {

    private final JsonObject json;

    public JsonConfiguration() {
        this.json = new JsonObject();
    }

    public void load(@NotNull File file) {
        try (Reader reader = new FileReader(file)) {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(reader);
            if (element.isJsonObject()) {
                this.json.add("data", element.getAsJsonObject());
            }
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }
    }

    public void save(@NotNull File file) {
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this.json.get("data"), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(@NotNull String path, @Nullable Object value) {
        this.json.getAsJsonObject("data").addProperty(path, value.toString());
    }

    public String getString(@NotNull String path, @Nullable String def) {
        if (this.json.getAsJsonObject("data").has(path)) {
            return this.json.getAsJsonObject("data").get(path).getAsString();
        }
        return def;
    }

    public String getString(@NotNull String path) {
        return getString(path, null);
    }

    public JsonArray getMapList(@NotNull String path) {
        if (this.json.getAsJsonObject("data").has(path)) {
            return this.json.getAsJsonObject("data").getAsJsonArray(path);
        }
        return new JsonArray();
    }

    public JsonElement get(String key) {
        return key == null ? null : json.get(key);
    }

    public JsonObject getMap(@NotNull final String path) {
        JsonObject data = json.getAsJsonObject("data");
        if (data != null && data.has(path)) {
            return data.getAsJsonObject(path);
        }
        return null;
    }


    public void setMap(@NotNull final JsonObject jsonObject) {
        JsonObject dataObject = this.json.getAsJsonObject("data");
        if (dataObject == null) {
            dataObject = new JsonObject();
            this.json.add("data", dataObject);
        }
        // Iteriere über die Einträge des übergebenen JsonObject und füge sie dem dataObject hinzu
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            dataObject.add(entry.getKey(), entry.getValue());
        }
    }

}
