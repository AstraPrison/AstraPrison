package dev.fabled.astra.utils.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class JsonConfiguration {

    private @NotNull Map<String, Object> map;

    JsonConfiguration() {
        map = new HashMap<>();
    }

    void setMap(@NotNull final Map<String, Object> map) {
        this.map = map;
    }

    @NotNull Map<String, Object> getMap() {
        return map;
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

    public String getString(@NotNull final String path, @Nullable final String def) {
        return get(path, def, String.class);
    }

    public String getString(@NotNull final String path) {
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

    public @NotNull Set<String> getSet(@NotNull final String parent) {
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

    private <T> T get(@NotNull final String path, T def, Class<T> clazz) {
        final Object object = map.getOrDefault(path, null);
        if (!clazz.isInstance(object)) {
            return def;
        }

        return clazz.cast(object);
    }

}
