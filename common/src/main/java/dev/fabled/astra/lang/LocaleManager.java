package dev.fabled.astra.lang;

import dev.fabled.astra.lang.annotations.MessageKey;
import dev.fabled.astra.lang.interfaces.MessageKeys;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public class LocaleManager {

    private final YamlConfig groupsYml;
    private final List<String> keys;
    private final Map<String, String> groupsByISO;
    private final Map<String, LocaleGroup> groups;

    public LocaleManager() {
        groupsYml = new YamlConfig("lang/groups");
        keys = new ArrayList<>();
        groupsByISO = new HashMap<>();
        groups = new HashMap<>();
    }

    public void onLoad() {
        onReload();
    }

    public void onReload() {
        groupsYml.save();
        groupsYml.reload();

        groupsByISO.clear();
        groups.clear();

        final ConfigurationSection section = groupsYml.options().getConfigurationSection("groups");
        if (section == null) {
            return;
        }

        section.getKeys(false).forEach(string -> {

        });
    }

    public void registerKeys(@NotNull final MessageKeys keyClass) {
        final Class<?> clazz = keyClass.getClass();
        for (final Field field : clazz.getFields()) {
            if (!field.isAnnotationPresent(MessageKey.class)) {
                continue;
            }

            if (!field.getType().equals(String.class)) {
                continue;
            }

            try { this.keys.add((String) field.get(clazz)); }
            catch (IllegalArgumentException | IllegalAccessException ignored) {}
        }
    }

    public @NotNull List<String> getKeys() {
        return new ArrayList<>(keys);
    }

    public static @NotNull String getLocale(@NotNull final Player player) {
        final Locale locale = player.locale();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

}
