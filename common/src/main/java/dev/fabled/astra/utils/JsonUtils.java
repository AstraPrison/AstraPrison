package dev.fabled.astra.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class JsonUtils {

    private static final @NotNull Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Convert a {@link JsonObject} to a {@link Map}<{@link String}, {@link Object}>
     * @param jsonObject {@link JsonObject}
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    public static @NotNull Map<String, Object> toMap(final @NotNull JsonObject jsonObject) {
        final Map<String, Object> map = new HashMap<>();
        populateMap(jsonObject, map, "");
        return map;
    }

    /**
     * Convert a JSON String to a {@link Map}<{@link String}, {@link Object}>
     * @param jsonString {@link String} in JSON format
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    public static @NotNull Map<String, Object> toMap(final @NotNull String jsonString) {
        return toMap(GSON.fromJson(jsonString, JsonObject.class));
    }

    /**
     * Convert a {@link Map}<{@link String}, {@link Object}> to a JSON String
     * @param map {@link Map}<{@link String}, {@link Object}>
     * @return {@link String} in JSON format
     */
    public static @NotNull String toJsonString(final @NotNull Map<String, Object> map) {
        final Map<String, Object> jsonMap = new HashMap<>();

        map.forEach((key, value) -> {
            final String[] keys = key.split("\\.");
            populateJsonMap(jsonMap, keys, 0, value);
        });

        return GSON.toJson(jsonMap);
    }

    private static void populateMap(
            final @NotNull JsonElement jsonElement,
            final @NotNull Map<String, Object> map,
            final @NotNull String prefix
    ) {
        if (jsonElement.isJsonArray()) {
            map.put(prefix, jsonElement.getAsJsonArray());
            return;
        }

        if (!jsonElement.isJsonObject()) {
            map.put(prefix, jsonElement.getAsString());
            return;
        }

        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            populateMap(entry.getValue(), map, key);
        }
    }

    private static void populateJsonMap(
            final @NotNull Map<String, Object> jsonMap,
            final @NotNull String[] keys,
            final int index,
            final @NotNull Object value
    ) {
        if (index == keys.length - 1) {
            jsonMap.put(keys[index], value);
            return;
        }

        jsonMap.putIfAbsent(keys[index], new HashMap<String, Object>());
        //noinspection unchecked
        final Map<String, Object> map = (Map<String, Object>) jsonMap.get(keys[index]);

        populateJsonMap(map, keys, index + 1, value);
    }

}
