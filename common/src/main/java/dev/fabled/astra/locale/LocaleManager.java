package dev.fabled.astra.locale;

import dev.fabled.astra.Astra;
import dev.fabled.astra.locale.annotations.AstraMessageKey;
import dev.fabled.astra.locale.impl.*;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import dev.fabled.astra.utils.logger.AstraLog;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public final class LocaleManager {

    private static LocaleManager instance;

    public static @NotNull LocaleManager getInstance() {
        if (instance == null) {
            instance = new LocaleManager();
            instance.onReload();
        }

        return instance;
    }

    private final @NotNull List<String> messageKeys;

    /**
     * A map of {@link AstraLocale} stored by Locale Group {@link String}
     */
    private final @NotNull Map<String, AstraLocale> locales;

    /**
     * A map of Locale Group {@link String} stored by Locale {@link String}
     */
    private final @NotNull Map<String, String> localeGroups;

    private @NotNull String defaultLocaleGroup;
    private @NotNull YamlConfig langYml;

    private LocaleManager() {
        messageKeys = new ArrayList<>();
        locales = new HashMap<>();
        localeGroups = new HashMap<>();
        langYml = new YamlConfig("locale/lang.yml");
        defaultLocaleGroup = "english";
        registerKeys(AdminMessageKeys.class);
        registerKeys(MineWandMessageKeys.class);
        registerKeys(OmniToolMessageKeys.class);
        registerKeys(SellPriceMessageKeys.class);
        registerKeys(ErrorMessageKeys.class);
    }

    public void registerKeys(final @NotNull Class<?> clazz) {
        final Field[] fields = clazz.getDeclaredFields();

        for (final Field field : fields) {
            if (!field.isAnnotationPresent(AstraMessageKey.class)) {
                continue;
            }

            if (field.getType() != String.class) {
                continue;
            }

            field.setAccessible(true);
            final String key;
            try { key = field.get(clazz).toString(); }
            catch (IllegalAccessException e) {
                AstraLog.log(e);
                continue;
            }

            messageKeys.add(key);
        }
    }

    public void onReload() {
        locales.clear();

        final File folder = new File(Astra.getPlugin().getDataFolder(), "locale");
        if (!folder.exists()) {
            langYml = new YamlConfig("locale/lang.yml");
            createDefaultLocaleGroup();
            reloadLocaleGroups();
            return;
        }

        final File[] files = folder.listFiles();
        if (files == null) {
            createDefaultLocaleGroup();
            reloadLocaleGroups();
            return;
        }

        for (final File file : files) {
            final String name = file.getName();
            if (!name.startsWith("locale-") || !name.endsWith(".yml")) {
                continue;
            }

            final String locale = name.substring(7, name.length() - 4).toLowerCase();
            locales.put(locale, new AstraLocale(
                    new YamlConfig("locale/" + name),
                    locale
            ));
        }

        if (!locales.containsKey(defaultLocaleGroup)) {
            createDefaultLocaleGroup();
        }

        reloadLocaleGroups();
    }

    private void reloadLocaleGroups() {
        langYml.save();
        langYml.reload();

        localeGroups.clear();
        defaultLocaleGroup = langYml.options().getString("default-group", "english").toLowerCase();

        final ConfigurationSection section = langYml.options().getConfigurationSection("locales");
        if (section == null) {
            return;
        }

        section.getKeys(false).forEach(key -> {
            final List<String> locales = ListUtils.fromConfig(langYml, "locales." + key);
            if (locales == null) {
                return;
            }

            locales.forEach(string -> localeGroups.put(string.toLowerCase(), key));
        });

        if (!locales.containsKey(defaultLocaleGroup)) {
            if (!locales.containsKey("english")) {
                createDefaultLocaleGroup();
            }

            defaultLocaleGroup = "english";
        }
    }

    private void createDefaultLocaleGroup() {
        locales.put("english", new AstraLocale(
                new YamlConfig("locale/locale-english.yml"),
                "english"
        ));
    }

    public @NotNull List<String> getMessageKeys() {
        return new ArrayList<>(messageKeys);
    }

    public @NotNull AstraLocale getLocale(final @NotNull String localeGroup) {
        return locales.getOrDefault(localeGroup.toLowerCase(), locales.get(defaultLocaleGroup));
    }

    public @NotNull String getLocaleGroup(final @NotNull String locale) {
        return localeGroups.getOrDefault(locale.toLowerCase(), defaultLocaleGroup);
    }

    public @NotNull String getLocale(final @NotNull Player player) {
        final Locale locale = player.locale();
        return (locale.getLanguage() + "_" + locale.getCountry()).toLowerCase();
    }

    public static void sendMessage(final @NotNull Player player, final @NotNull String key, final @NotNull List<String> replacements) {
        final LocaleManager manager = LocaleManager.getInstance();
        final String locale = manager.getLocale(player);
        final String group = manager.getLocaleGroup(locale);
        final AstraLocale astraLocale = manager.getLocale(group);

        final List<String> rep = new ArrayList<>(replacements);
        rep.add("{PREFIX}");
        rep.add(astraLocale.getPrefix());

        final List<String> message = PapiUtils.parse(
                ListUtils.replace(astraLocale.getMessage(key), rep),
                player
        );

        final String m = String.join("<newline>", message);
        player.sendMessage(MiniColor.CHAT.deserialize(m));
    }

    public static void sendMessage(final @NotNull Player player, final @NotNull String key, final @NotNull String... replacements) {
        sendMessage(player, key, List.of(replacements));
    }

    public static void sendMessage(final @NotNull Player player, final @NotNull String key) {
        sendMessage(player, key, Collections.emptyList());
    }

}
