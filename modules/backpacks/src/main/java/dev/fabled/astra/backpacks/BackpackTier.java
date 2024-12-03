package dev.fabled.astra.backpacks;

import dev.fabled.astra.modules.impl.BackpacksModule;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BackpackTier {

    private static final @NotNull BackpackTier DEFAULT;

    static {
        DEFAULT = new BackpackTier(0, Material.CHEST, 0, "Backpack (I)", 64);
    }

    public static @NotNull BackpackTier getDefault() {
        return new BackpackTier(DEFAULT);
    }

    private final int tier;
    private @NotNull Material material;
    private final int customModelData;
    private final @Nullable String displayName;
    private final int maxCapacity;

    public BackpackTier(
            final int tier,
            final @NotNull Material material,
            final int customModelData,
            final @Nullable String displayName,
            final int maxCapacity
    ) {
        this.tier = tier;
        this.material = material;
        this.customModelData = customModelData;
        this.displayName = displayName;
        this.maxCapacity = maxCapacity;
    }

    private BackpackTier(final @NotNull BackpackTier backpackTier) {
        this(
                backpackTier.tier,
                backpackTier.material,
                backpackTier.customModelData,
                backpackTier.displayName,
                backpackTier.maxCapacity
        );
    }

    public BackpackTier(final @NotNull YamlConfig config, @NotNull String path, final int tier) {
        this.tier = tier;
        if (!path.endsWith(".")) {
            path += ".";
        }

        try {
            material = Material.valueOf(config.options().getString(path + "material", "CHEST"));
        }
        catch (IllegalArgumentException e) {
            material = Material.CHEST;
        }

        customModelData = config.options().getInt(path + "custom-model-data", 0);
        displayName = config.options().getString(path + "display-name", null);
        maxCapacity = config.options().getInt(path + "max-capacity", 64);
    }

    public @NotNull ItemStack createBackpackItem() {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;

        if (displayName != null) {
            meta.displayName(MiniColor.INVENTORY.deserialize(displayName));
        }

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(
                BackpacksModule.getInstance().getBackpackKey(),
                PersistentDataType.INTEGER,
                tier
        );
//        container.set(
//                BackpacksModule.getInstance().getBackpackContentsKey(),
//
//        );

        item.setItemMeta(meta);
        return item;
    }

    public int getTier() {
        return tier;
    }

    public @NotNull Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public @Nullable String getDisplayName() {
        return displayName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

}
