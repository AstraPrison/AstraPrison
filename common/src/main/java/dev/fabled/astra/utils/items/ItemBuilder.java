package dev.fabled.astra.utils.items;

import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    /*
            TODO
            Add PDC support
            Add Base64 head support
            Add player-specific head support
     */

    private String material;
    private int customModelData;
    private String displayName;
    private List<String> lore;

    public ItemBuilder(final @NotNull String material) {
        this.material = material;
    }

    public ItemBuilder(final @NotNull YamlConfig config, @NotNull String key) {
        if (!key.endsWith(".")) {
            key += ".";
        }

        material = config.options().getString(key + "material", MaterialUtils.DEFAULT_MATERIAL.name());
        customModelData = config.options().getInt(key + "custom-model-data", 0);
        displayName = config.options().getString(key + "display-name", null);
        lore = ListUtils.fromConfig(config, key + "lore");
    }

    public @NotNull ItemBuilder setMaterial(final @NotNull String material) {
        this.material = material;
        return this;
    }

    public @NotNull ItemBuilder setMaterial(final @NotNull Material material) {
        this.material = material.name();
        return this;
    }

    public @NotNull ItemBuilder setCustomModelData(final int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public @NotNull ItemBuilder setDisplayName(final @Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    public @NotNull ItemBuilder setLore(final @Nullable List<String> lore) {
        if (lore == null) {
            this.lore = null;
            return this;
        }

        this.lore = new ArrayList<>(lore);
        return this;
    }

    public @NotNull ItemBuilder setLore(final @Nullable String... lore) {
        return setLore(List.of(lore));
    }

    public @NotNull ItemBuilder addLore(final @NotNull List<String> lore) {
        if (this.lore == null) {
            this.lore = new ArrayList<>();
        }

        this.lore.addAll(lore);
        return this;
    }

    public @NotNull ItemBuilder addLore(final @NotNull String... lore) {
        return addLore(List.of(lore));
    }

    public @NotNull ItemStack build(final @Nullable Player player) {
        final ItemStack item = MaterialUtils.parse(material, player);
        final ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(customModelData);

        if (displayName != null) {
            meta.displayName(MiniColor.INVENTORY.deserialize(
                    PapiUtils.parse(displayName, player)
            ));
        }

        if (lore != null) {
            meta.lore(MiniColor.INVENTORY.deserialize(
                    PapiUtils.parse(lore, player)
            ));
        }

        item.setItemMeta(meta);
        return item;
    }

    public @NotNull ItemStack build() {
        return build(null);
    }

}
