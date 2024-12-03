package dev.fabled.astra.backpacks;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Backpack {

    private final @NotNull String name;
    private final @NotNull Map<Integer, BackpackTier> tiers;

    public Backpack(final @NotNull YamlConfig config, final @NotNull String name) {
        this.name = name;
        tiers = new HashMap<>();

        final String path = "backpacks." + name;
        final ConfigurationSection section = config.options().getConfigurationSection(path);
        if (section == null) {
            tiers.put(0, BackpackTier.getDefault());
            return;
        }

        final AtomicInteger index = new AtomicInteger(0);
        section.getKeys(false).forEach(key -> {
            final int i = index.get();
            tiers.put(i, new BackpackTier(config, path + "." + key + ".", i));
            index.setPlain(i+1);
        });
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Map<Integer, BackpackTier> getTiers() {
        return new HashMap<>(tiers);
    }

    public boolean hasTier(final int tier) {
        return tiers.containsKey(tier);
    }

    public @Nullable BackpackTier getTier(final int tier) {
        return tiers.getOrDefault(tier, null);
    }

    public boolean hasNextTier(final int currentTier) {
        return tiers.containsKey(currentTier + 1);
    }

}
