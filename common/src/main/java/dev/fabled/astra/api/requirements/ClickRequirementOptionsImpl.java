package dev.fabled.astra.api.requirements;

import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ClickRequirementOptionsImpl implements ClickRequirementOptions {

    private final @NotNull Map<String, Object> options;

    public ClickRequirementOptionsImpl(final @NotNull YamlConfig config, final @NotNull String key) {
        options = new HashMap<>();

        final ConfigurationSection section = config.options().getConfigurationSection(key);
        if (section == null) {
            return;
        }

        section.getKeys(false).forEach(option ->
                options.put(option, config.options().get(key + "." + option))
        );
    }

    @Override
    public @NotNull Boolean contains(@NotNull String key) {
        return options.containsKey(key);
    }

    @Override
    public @Nullable Object getObject(@NotNull String key, @Nullable Object def) {
        return options.getOrDefault(key, def);
    }

    @Override
    public @Nullable String getString(@NotNull String key, @Nullable String def) {
        final Object object = options.getOrDefault(key, null);
        if (object == null) {
            return def;
        }

        return object.toString();
    }

    @Override
    public @NotNull Boolean getBoolean(@NotNull String key, @NotNull Boolean def) {
        final Object object = options.getOrDefault(key, null);
        return object instanceof Boolean bool ? bool : def;
    }

    @Override
    public @NotNull Integer getInt(@NotNull String key, @NotNull Integer def) {
        final Object object = options.getOrDefault(key, null);
        return object instanceof Integer i ? i : def;
    }

    @Override
    public @NotNull Double getDouble(@NotNull String key, @NotNull Double def) {
        final Object object = options.getOrDefault(key, null);
        return object instanceof Double i ? i : def;
    }

    @Override
    public @Nullable List<String> getStringList(@NotNull String key, @Nullable List<String> def) {
        final Object object = options.getOrDefault(key, null);
        if (object instanceof List<?> list) {
            return ListUtils.convertToStringList(list);
        }

        if (object == null) {
            return def;
        }

        return new ArrayList<>(Collections.singletonList(object.toString()));
    }

}
