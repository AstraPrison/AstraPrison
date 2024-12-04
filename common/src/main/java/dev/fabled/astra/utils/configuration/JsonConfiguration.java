package dev.fabled.astra.utils.configuration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class JsonConfiguration {

    private @NotNull Map<String, Object> map;

    public JsonConfiguration() {
        map = new HashMap<>();
    }

    void setMap(final @NotNull Map<String, Object> newMap) {
        map = newMap;
    }

    @NotNull Map<String, Object> getMap() {
        return map;
    }

    public void set(final @NotNull String path, final @Nullable Object object) {
        if (object == null) {
            map.remove(path);
            return;
        }

        map.put(path, object);
    }

    @Contract("_, !null, _ -> !null")
    private <T> T get(final @NotNull String path, final @Nullable T def, final @NotNull Class<T> clazz) {
        final Object object = map.getOrDefault(path, null);
        if (!clazz.isInstance(object)) {
            return def;
        }

        return clazz.cast(object);
    }

    public boolean has(final @NotNull String key) {
        return map.containsKey(key);
    }

    @Contract("_, !null -> !null")
    public @Nullable Object get(final @NotNull String path, final @Nullable Object def) {
        return map.getOrDefault(path, def);
    }

    public @Nullable Object get(final @NotNull String path) {
        return get(path, null);
    }

    public @Nullable String getString(final @NotNull String path, final @Nullable String def) {
        return get(path, def, String.class);
    }

    public @Nullable String getString(final @NotNull String path) {
        return getString(path, null);
    }

    public boolean getBoolean(@NotNull final String path, final boolean def) {
        return get(path, def, Boolean.class);
    }

    public boolean getBoolean(@NotNull final String path) {
        return getBoolean(path, false);
    }

    public int getByte(@NotNull final String path, final byte def) {
        return get(path, def, Byte.class);
    }

    public int getByte(@NotNull final String path) {
        return getByte(path, (byte) 0);
    }

    public int getInteger(@NotNull final String path, final int def) {
        return get(path, def, Integer.class);
    }

    public int getInteger(@NotNull final String path) {
        return getInteger(path, 0);
    }

    public long getLong(@NotNull final String path, final long def) {
        return get(path, def, Long.class);
    }

    public long getLong(@NotNull final String path) {
        return getLong(path, 0L);
    }

    public float getFloat(@NotNull final String path, final float def) {
        return get(path, def, Float.class);
    }

    public float getFloat(@NotNull final String path) {
        return getFloat(path, 0f);
    }

    public double getDouble(@NotNull final String path, final double def) {
        return get(path, def, Double.class);
    }

    public double getDouble(@NotNull final String path) {
        return getDouble(path, 0d);
    }

    public @NotNull Set<String> getSet(final @NotNull String parent) {
        final Set<String> set = new HashSet<>();

        map.keySet().forEach(key -> {
            if (!key.startsWith(parent)) {
                return;
            }

            final String sub = key.substring(parent.length());
            final String[] split = sub.split("\\.");

            if (split.length > 0) {
                set.add(split[0]);
                return;
            }

            set.add(sub);
        });

        return set;
    }

    public void clear() {
        map.clear();
    }

}
