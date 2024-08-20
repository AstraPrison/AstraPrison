package dev.fabled.astra.menus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuManager {

    private final @NotNull Map<String, AstraMenu> menus;

    public MenuManager() {
        menus = new HashMap<>();
    }

    public void register(final @NotNull AstraMenu menu) {
        menus.put(menu.id, menu);
    }

    public boolean exists(final @NotNull String id) {
        return menus.containsKey(id);
    }

    public @Nullable AstraMenu getMenu(final @NotNull String id) {
        return menus.getOrDefault(id, null);
    }

    public @NotNull List<String> getMenuIDs() {
        return new ArrayList<>(menus.keySet());
    }

    public @NotNull List<AstraMenu> getMenus() {
        return new ArrayList<>(menus.values());
    }

}