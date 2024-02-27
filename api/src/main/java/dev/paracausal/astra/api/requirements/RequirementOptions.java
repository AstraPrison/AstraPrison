package dev.paracausal.astra.api.requirements;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequirementOptions {

    private @NotNull final FileConfiguration config;
    private @NotNull final String basePath;

    public RequirementOptions(@NotNull final FileConfiguration config, @NotNull final String basePath) {
        this.config = config;
        this.basePath = basePath.endsWith(".") ? basePath : basePath + ".";
    }

    public boolean contains(@NotNull final String path) {
        return config.contains(basePath + path);
    }

    public @Nullable Object get(@NotNull final String path, @NotNull final Object def) {
        return config.get(basePath + path, def);
    }

    public @Nullable Object get(@NotNull final String path) {
        return config.get(basePath + path);
    }

    public @Nullable String getString(@NotNull final String path, @NotNull final String def) {
        return config.getString(basePath + path, def);
    }

    public @Nullable String getString(@NotNull final String path) {
        return config.getString(basePath + path);
    }

    public @Nullable List<String> getStringList(@NotNull final String path) {
        final Object object = config.get(basePath + path);
        if (object == null) {
            return null;
        }

        if (object instanceof List<?>) {
            return config.getStringList(basePath + path);
        }

        return new ArrayList<>(Collections.singleton(object.toString()));
    }

    public @NotNull List<String> getStringList(@NotNull final String path, @NotNull final List<String> def) {
        final List<String> list = getStringList(path);
        if (list == null) {
            return def;
        }

        return list;
    }

    public int getInt(@NotNull final String path, final int def) {
        return config.getInt(basePath + path, def);
    }

    public int getInt(@NotNull final String path) {
        return config.getInt(basePath + path);
    }

    public double getDouble(@NotNull final String path, final double def) {
        return config.getDouble(basePath + path, def);
    }

    public double getDouble(@NotNull final String path) {
        return config.getDouble(basePath + path);
    }

    public long getLong(@NotNull final String path, final long def) {
        return config.getLong(basePath + path, def);
    }

    public long getLong(@NotNull final String path) {
        return config.getLong(basePath + path);
    }

    public boolean getBoolean(@NotNull final String path, final boolean def) {
        return config.getBoolean(basePath + path, def);
    }

    public boolean getBoolean(@NotNull final String path) {
        return config.getBoolean(basePath + path);
    }

}
