package dev.fabled.astra.lang;

import dev.fabled.astra.Astra;
import dev.fabled.astra.lang.annotations.LangKey;
import dev.fabled.astra.lang.interfaces.LangKeys;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public final class LocaleManager {

    private static LocaleManager instance;
    private static YamlConfig groupsYml;

    public static LocaleManager getInstance() {
        return instance;
    }

    public static void onLoad() {
        if (instance != null) {
            return;
        }

        groupsYml = new YamlConfig("lang/groups");
        instance = new LocaleManager();
    }

    final Map<String, LocaleGroup> localeGroups;
    final Map<String, String> localeGroupByIso;
    final Set<String> messageKeys;
    String defLocaleGroup;

    LocaleManager() {
        localeGroups = new HashMap<>();
        localeGroupByIso = new HashMap<>();
        messageKeys = new HashSet<>();
    }

    public void registerLanguageKeys(@NotNull final LangKeys keys) {
        final Class<?> clazz = keys.getClass();
        final Field[] fields = clazz.getFields();

        for (final Field field : fields) {
            if (!field.isAnnotationPresent(LangKey.class)) {
                continue;
            }

            if (!field.getType().equals(String.class)) {
                continue;
            }

            try { messageKeys.add((String) field.get(clazz)); }
            catch (IllegalArgumentException | IllegalAccessException ignored) {}
        }
    }

    public void onEnable() {
        onReload();
    }

    public void onReload() {
        localeGroups.clear();
        localeGroupByIso.clear();
        LocaleManager.groupsYml.save();
        LocaleManager.groupsYml.reload();
        defLocaleGroup = groupsYml.options().getString("default", "english");

        final File folder = new File(Astra.getPlugin().getDataFolder(), "lang");
        final File[] files = folder.listFiles();

        if (files == null || files.length < 2) {
            localeGroups.put("english", new LocaleGroup(new YamlConfig("lang/lang-english")));
            defLocaleGroup = "english";
            return;
        }

        for (final File file : files) {
            final String fileName = file.getName();
            final String lowerFileName = fileName.toLowerCase();
            if (!lowerFileName.startsWith("lang-")) {
                continue;
            }

            if (!lowerFileName.endsWith(".yml")) {
                continue;
            }

            final String groupName = fileName.substring(5, fileName.length() - 4);

            final List<String> groupISOs = ListUtils.fromConfigString(LocaleManager.groupsYml, "groups." + groupName, ",");
            if (groupISOs == null) {
                continue;
            }

            final YamlConfig groupYml = new YamlConfig("lang/" + fileName);
            final LocaleGroup group = new LocaleGroup(groupYml);

            localeGroups.put(groupName, group);
            groupISOs.forEach(iso -> localeGroupByIso.put(iso, groupName));
        }

        if (!localeGroups.containsKey(defLocaleGroup)) {
            AstraLog.log(AstraLogLevel.ERROR, "The default language group does not have its own file!");
            defLocaleGroup = localeGroups.keySet().stream().toList().getFirst();
            AstraLog.log("Set the default language to " + defLocaleGroup + "!");
        }
    }

    @NotNull LocaleGroup getDefaultLocaleGroup() {
        return localeGroups.get(defLocaleGroup);
    }

    private static @NotNull LocaleGroup getLocaleGroup(@NotNull final Player player) {
        final Locale locale = player.locale();
        final LocaleManager instance = LocaleManager.getInstance();

        final String localeGroup = instance.localeGroupByIso.getOrDefault(
                locale.getLanguage() + "_" + locale.getCountry(),
                instance.defLocaleGroup
        );

        return instance.localeGroups.get(localeGroup);
    }

    public static void send(
            @NotNull final Player player,
            @NotNull final String key,
            @Nullable final String... replacements
    ) {
        final LocaleGroup localeGroup = getLocaleGroup(player);
        final String prefix = localeGroup.getPrefix();

        List<String> message = ListUtils.replace(localeGroup.getMessage(key), replacements);
        message = PapiUtils.parse(player, message);

        for (String string : message) {
            string = string.replace("{PREFIX}", prefix);
            player.sendMessage(MiniColor.parse(string));
        }
    }

}
