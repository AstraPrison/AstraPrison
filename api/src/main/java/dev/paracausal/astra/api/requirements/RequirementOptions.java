package dev.paracausal.astra.api.requirements;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RequirementOptions {

    private @NotNull final FileConfiguration config;
    private @NotNull final String basePath;

    public RequirementOptions(@NotNull final FileConfiguration config, @NotNull final String basePath) {
        this.config = config;
        this.basePath = basePath.endsWith(".") ? basePath : basePath + ".";
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
