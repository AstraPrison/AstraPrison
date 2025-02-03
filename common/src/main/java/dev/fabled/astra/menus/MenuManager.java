package dev.fabled.astra.menus;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MenuManager {

    private static final @NotNull String CUSTOM_MENUS_FOLDER;

    static {
        CUSTOM_MENUS_FOLDER = "menus/custom";
    }

    private static MenuManager instance;

    public static @NotNull MenuManager getInstance() {
        if (instance == null) {
            instance = new MenuManager();
        }

        return instance;
    }

    private final @NotNull Map<String, AstraMenu> menus;

    private MenuManager() {
        menus = new HashMap<>();
        onReload();
    }

    public void onReload() {
        AstraMenu.closeAll();
        menus.clear();

        final File folder = new File(Astra.getPlugin().getDataFolder(), CUSTOM_MENUS_FOLDER);
        if (!folder.exists()) {
            menus.put("example", new AstraMenu(new YamlConfig(CUSTOM_MENUS_FOLDER + "/example.yml")));
            return;
        }

        final File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        for (final File file : files) {
            final String fileName = file.getName();
            if (fileName.startsWith("_") || !fileName.endsWith(".yml")) {
                continue;
            }

            final String menuId = fileName.substring(0, fileName.length() - 4).toLowerCase();
            menus.put(menuId, new AstraMenu(new YamlConfig(CUSTOM_MENUS_FOLDER + "/" + fileName)));
        }
    }

    public boolean exists(final @NotNull String id) {
        return menus.containsKey(id.toLowerCase());
    }

    public @Nullable AstraMenu getMenu(final @NotNull String id) {
        return menus.getOrDefault(id.toLowerCase(), null);
    }

    public @NotNull List<String> getMenuIDs() {
        return new ArrayList<>(menus.keySet());
    }

}
