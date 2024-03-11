package dev.paracausal.astra.utilities.items;

import dev.paracausal.astra.exceptions.InvalidMaterialException;
import dev.paracausal.astra.utilities.ListUtils;
import dev.paracausal.astra.utilities.MiniColor;
import dev.paracausal.astra.utilities.configuration.YamlConfig;
import dev.paracausal.astra.utilities.dependencies.PapiUtils;
import dev.paracausal.astra.utilities.items.pdc.ItemPDC;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    public static final MaterialBox DEFAULT_MATERIAL;

    static {
        DEFAULT_MATERIAL = new MaterialBox(Material.STONE);
    }

    private MaterialBox materialBox;
    private int customModelData;
    private String displayName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;
    private int amount;
    private List<ItemPDC<?>> pdc;



    /*
            Config Format
            path would be "item" or "item."

            item:
              material: ''                     String
              custom-model-data: 0             Integer
              display-name: ''                 String
              lore: ''                         String or String List
              enchantments: ''                 String or String List
              item-flags: ''                   String or String List
              amount: 1                        Integer
     */
    public ItemBuilder(@NotNull final YamlConfig config, @NotNull String path) {
        if (!path.endsWith(".")) path += ".";
        MaterialBox matBox;
        try { matBox = new MaterialBox(config.options().getString(path + "material", "STONE")); }
        catch (InvalidMaterialException e) {
            matBox = new MaterialBox(DEFAULT_MATERIAL);
        }

        materialBox = matBox;
        customModelData = config.options().getInt(path + "custom-model-data", 0);
        displayName = config.options().getString(path + "display-name", null);
        lore = ListUtils.fromConfig(config, path + "lore", null);
        enchantments = EnchantUtils.parse(ListUtils.fromConfig(config, path + "lore", null));
        itemFlags = ItemFlagUtils.parse(ListUtils.fromConfig(config, path + "item-flags", null));
        setAmount(config.options().getInt(path + "amount", 1));
    }

    public ItemBuilder() {}

    public ItemBuilder(@NotNull final MaterialBox materialBox) {
        this.materialBox = materialBox;
    }

    public ItemBuilder(@NotNull final Material material) {
        materialBox = new MaterialBox(material);
    }

    public ItemBuilder(@NotNull final String material) {
        try { materialBox = new MaterialBox(material); }
        catch (InvalidMaterialException e) {
            materialBox = DEFAULT_MATERIAL;
        }
    }



    public ItemBuilder setMaterial(@NotNull final MaterialBox materialBox) {
        this.materialBox = materialBox;
        return this;
    }

    public ItemBuilder setMaterial(@NotNull final Material material) {
        materialBox = new MaterialBox(material);
        return this;
    }

    public ItemBuilder setMaterial(@NotNull final String material) {
        try { materialBox = new MaterialBox(material); }
        catch (InvalidMaterialException e) {
            materialBox = DEFAULT_MATERIAL;
        }

        return this;
    }



    public ItemBuilder setCustomModelData(final int customModelData) {
        this.customModelData = customModelData;
        return this;
    }



    public ItemBuilder setDisplayName(@Nullable final String displayName) {
        this.displayName = displayName;
        return this;
    }



    public ItemBuilder setLore(@Nullable final List<String> lore) {
        if (lore == null) {
            this.lore = null;
        }

        else {
            this.lore = new ArrayList<>(lore);
        }

        return this;
    }

    public ItemBuilder addLore(@NotNull final List<String> lore) {
        if (this.lore == null) {
            this.lore = new ArrayList<>(lore);
        }

        else {
            this.lore.addAll(lore);
        }

        return this;
    }

    public ItemBuilder addLore(@NotNull final String... loreLine) {
        if (this.lore == null) {
            this.lore = new ArrayList<>(List.of(loreLine));
        }

        else {
            this.lore.addAll(List.of(loreLine));
        }

        return this;
    }



    public ItemBuilder setEnchantments(@Nullable final Map<Enchantment, Integer> enchantments) {
        if (enchantments == null) {
            this.enchantments = null;
        }

        else {
            this.enchantments = new HashMap<>(enchantments);
        }

        return this;
    }

    public ItemBuilder addEnchantments(@NotNull final Map<Enchantment, Integer> enchantments) {
        if (this.enchantments == null) {
            this.enchantments = new HashMap<>(enchantments);
        }

        else {
            this.enchantments.putAll(enchantments);
        }

        return this;
    }

    public ItemBuilder addEnchantments(final int level, @NotNull final Enchantment... enchantments) {
        if (this.enchantments == null) {
            this.enchantments = new HashMap<>();
        }

        for (final Enchantment enchantment : enchantments) {
            this.enchantments.put(enchantment, level);
        }

        return this;
    }



    public ItemBuilder setItemFlags(@Nullable final List<ItemFlag> itemFlags) {
        if (itemFlags == null) {
            this.itemFlags = null;
        }

        else {
            this.itemFlags = new ArrayList<>(itemFlags);
        }

        return this;
    }

    public ItemBuilder addItemFlags(@NotNull final List<ItemFlag> itemFlags) {
        if (this.itemFlags == null) {
            this.itemFlags = new ArrayList<>(itemFlags);
        }

        else {
            this.itemFlags.addAll(itemFlags);
        }

        return this;
    }

    public ItemBuilder addItemFlags(@NotNull final ItemFlag... itemFlags) {
        if (this.itemFlags == null) {
            this.itemFlags = new ArrayList<>(List.of(itemFlags));
        }

        else {
            this.itemFlags.addAll(List.of(itemFlags));
        }

        return this;
    }



    public ItemBuilder setAmount(int amount) {
        amount = Math.max(1, amount);
        amount = Math.max(64, amount);
        this.amount = amount;
        return this;
    }



    public ItemBuilder setPDC(@Nullable final List<ItemPDC<?>> pdc) {
        if (pdc == null) {
            this.pdc = null;
        }

        else {
            this.pdc = new ArrayList<>(pdc);
        }

        return this;
    }

    public ItemBuilder addPDC(@NotNull final List<ItemPDC<?>> pdc) {
        if (this.pdc == null) {
            this.pdc = new ArrayList<>(pdc);
        }

        else {
            this.pdc.addAll(pdc);
        }

        return this;
    }

    public ItemBuilder addPDC(@NotNull final ItemPDC<?>... pdc) {
        if (this.pdc == null) {
            this.pdc = new ArrayList<>(List.of(pdc));
        }

        else {
            this.pdc.addAll(List.of(pdc));
        }

        return this;
    }



    public ItemStack build(@Nullable final OfflinePlayer player, @Nullable String templateReplacement) {
        final MaterialBox mat = materialBox != null ? materialBox : DEFAULT_MATERIAL;
        final ItemStack itemStack = MaterialBoxUtils.parseMaterial(mat, player, templateReplacement);

        final ItemMeta meta = itemStack.getItemMeta();
        meta.setCustomModelData(customModelData);

        if (displayName != null) {
            meta.displayName(MiniColor.COLOR.deserialize(PapiUtils.parse(player, displayName)));
        }

        if (lore != null) {
            final List<Component> parsed = new ArrayList<>();
            lore.forEach(string -> {
                string = PapiUtils.parse(player, string);
                parsed.add(MiniColor.COLOR.deserialize(string));
            });

            meta.lore(parsed);
        }

        if (enchantments != null) {
            enchantments.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));
        }

        if (itemFlags != null) {
            itemFlags.forEach(meta::addItemFlags);
        }

        if (pdc != null) {
            final PersistentDataContainer container = meta.getPersistentDataContainer();
            pdc.forEach(pdc -> pdc.modify(container));
        }

        itemStack.setItemMeta(meta);
        itemStack.setAmount(Math.max(1, amount));

        return itemStack;
    }

    public ItemStack build(@Nullable OfflinePlayer player) {
        return build(player, null);
    }

    public ItemStack build() {
        return build(null, null);
    }

}
