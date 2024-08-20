package dev.fabled.astra.utils.items;

import dev.fabled.astra.exceptions.InvalidMaterialException;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import dev.fabled.astra.utils.logger.AstraLog;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemBuilder {

    private @NotNull MaterialBox materialBox;
    private int customModelData;
    private @Nullable String displayName;
    private @Nullable List<String> lore;
    private @Nullable Map<Enchantment, Integer> enchantments;
    private @Nullable Set<ItemFlag> itemFlags;

    public ItemBuilder() {
        materialBox = new MaterialBox(ItemStackUtils.DEFAULT_MATERIAL);
    }

    public ItemBuilder(@NotNull final MaterialBox materialBox) {
        this.materialBox = materialBox;
    }

    public ItemBuilder(@NotNull final Material material) {
        this.materialBox = new MaterialBox(material);
    }

    public ItemBuilder(@NotNull final String material) throws InvalidMaterialException {
        this.materialBox = new MaterialBox(material);
    }

    public ItemBuilder(@NotNull final YamlConfig config, @NotNull final String path) {
        try { materialBox = new MaterialBox(config.options().getString(path + ".material", "STONE")); }
        catch (InvalidMaterialException e) {
            AstraLog.log(e);
            materialBox = new MaterialBox(ItemStackUtils.DEFAULT_MATERIAL);
        }

        displayName = config.options().getString(path + ".display-name", null);
        lore = ListUtils.fromConfig(config, path + ".lore", null);
    }



    public @NotNull ItemBuilder setMaterial(@NotNull final MaterialBox materialBox) {
        this.materialBox = materialBox;
        return this;
    }

    public @NotNull ItemBuilder setMaterial(@NotNull final Material material) {
        this.materialBox = new MaterialBox(material);
        return this;
    }

    public @NotNull ItemBuilder setMaterial(@NotNull final String material) throws InvalidMaterialException {
        this.materialBox = new MaterialBox(material);
        return this;
    }

    public @NotNull MaterialBox getMaterial() {
        return materialBox;
    }



    public @NotNull ItemBuilder setCustomModelData(final int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public int getCustomModelData() {
        return customModelData;
    }



    public @NotNull ItemBuilder setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    public @Nullable String getDisplayName() {
        return displayName;
    }



    public @NotNull ItemBuilder setLore(@Nullable List<String> lore) {
        this.lore = lore == null ? null : new ArrayList<>(lore);
        return this;
    }

    public @Nullable List<String> getLore() {
        return lore == null ? null : new ArrayList<>(lore);
    }



    public @NotNull ItemBuilder setEnchantments(@Nullable Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments == null ? null : new HashMap<>(enchantments);
        return this;
    }

    public @Nullable Map<Enchantment, Integer> getEnchantments() {
        return enchantments == null ? null : new HashMap<>(enchantments);
    }



    public @NotNull ItemBuilder setItemFlags(@Nullable Set<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags == null ? null : new HashSet<>(itemFlags);
        return this;
    }

    public @Nullable Set<ItemFlag> getItemFlags() {
        return itemFlags == null ? null : new HashSet<>(itemFlags);
    }



    public @NotNull ItemStack build(@Nullable final Player player, @Nullable final String replacement) {
        final ItemStack item = MaterialBuilder.build(materialBox, player, replacement);
        final ItemMeta meta = item.getItemMeta();

        if (displayName != null) {
            meta.displayName(MiniColor.INVENTORY.deserialize(PapiUtils.parse(player, displayName)));
        }

        if (lore != null) {
            meta.lore(MiniColor.INVENTORY.deserialize(PapiUtils.parse(player, lore)));
        }

        item.setItemMeta(meta);
        return item;
    }

    public @NotNull ItemStack build(@Nullable final Player player) {
        return build(player, null);
    }

    public @NotNull ItemStack build() {
        return build(null, null);
    }

}
