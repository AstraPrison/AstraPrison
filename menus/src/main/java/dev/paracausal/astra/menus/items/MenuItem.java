package dev.paracausal.astra.menus.items;

import dev.paracausal.astra.Astra;
import dev.paracausal.astra.utilities.MenuSlotUtils;
import dev.paracausal.astra.utilities.configuration.YamlConfig;
import dev.paracausal.astra.utilities.items.ItemBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MenuItem extends ItemBuilder {

    private String id = UUID.randomUUID().toString();
    private List<Integer> slots;
    private MenuRequirements viewRequirements;
    private Map<MenuClickType, MenuClickRequirements> clickRequirements;
    private Map<MenuClickType, MenuClickActions> clickActions;



    public MenuItem setID(@NotNull final String id) {
        this.id = id;
        return this;
    }



    public @NotNull MenuItem setSlots(@Nullable final List<Integer> slots) {
        if (slots == null) {
            this.slots = null;
        }

        else {
            this.slots = new ArrayList<>(slots);
        }

        return this;
    }

    public @NotNull MenuItem setSlots(@NotNull final YamlConfig config, @NotNull String path) {
        if (!path.endsWith(".slots")) {
            path += ".slots";
        }

        slots = MenuSlotUtils.fromConfig(config, path);
        return this;
    }

    public @NotNull List<Integer> getSlots() {
        if (slots == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(slots);
    }



    /*

            TODO

            Setters for View Requirements
            Setters for Click Requirements
            Setters for Click Actions

     */



    public MenuItem setViewRequirements() {
        return this;
    }



    @Override
    public ItemStack build(@Nullable final OfflinePlayer player, @Nullable final String templateReplacement) {
        final ItemStack item = super.build(player, templateReplacement);

        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(Astra.getMenuItemNamespacedKey(), PersistentDataType.STRING, id);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack build(@Nullable OfflinePlayer player) {
        return build(player, null);
    }

    @Override
    public ItemStack build() {
        return build(null, null);
    }

}
