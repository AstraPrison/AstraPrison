package dev.fabled.astra.backpacks;

import dev.fabled.astra.modules.impl.BackpacksModule;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class BackpackManager {

    private final @NotNull Map<String, Backpack> backpacks;

    public BackpackManager() {
        backpacks = new HashMap<>();
        onReload();
    }

    public void onReload() {
        backpacks.clear();

        final YamlConfig config = BackpacksModule.getInstance().getBackpacksYml();
        final ConfigurationSection section = config.options().getConfigurationSection("backpacks");
        if (section == null) {
            return;
        }

        section.getKeys(false).forEach(key ->
                backpacks.put(key, new Backpack(config, key))
        );
    }

    public boolean exists(final @NotNull String id, final int tier) {
        final Backpack backpack = backpacks.getOrDefault(id, null);
        if (backpack == null) {
            return false;
        }

        return backpack.hasTier(tier);
    }

    public boolean exists(final @NotNull String id) {
        return backpacks.containsKey(id);
    }

    public @NotNull List<String> getBackpackIds() {
        return new ArrayList<>(backpacks.keySet());
    }

    public @Nullable Backpack getBackpack(final @NotNull String id) {
        return backpacks.getOrDefault(id, null);
    }

}
