package dev.paracausal.astra.menus.items;

import dev.paracausal.astra.Astra;
import dev.paracausal.astra.utilities.ListUtils;
import dev.paracausal.astra.utilities.configuration.YamlConfig;
import dev.paracausal.astra.utilities.items.ItemBuilder;
import dev.paracausal.astra.utilities.items.MaterialBox;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MenuItem extends ItemBuilder {

    private String id = UUID.randomUUID().toString();
    private List<Integer> slots;
    private MenuRequirements viewRequirements;
    private Map<MenuClickType, MenuClickRequirements> clickRequirements;
    private Map<MenuClickType, MenuClickActions> clickActions;



    public MenuItem(@NotNull final YamlConfig config, @NotNull String path) {
        super(config, path);

        if (!path.endsWith(".")) path += ".";
        viewRequirements = new MenuRequirements(config, path + "view-requirements");

        clickRequirements = new HashMap<>();
        clickActions = new HashMap<>();

        for (final MenuClickType clickType : MenuClickType.values()) {
            final String p = path + clickType.getConfigPath();

            if (config.options().contains(p + "-click-requirements")) {
                clickRequirements.put(clickType, new MenuClickRequirements(clickType, config, path));
            }

            if (!config.options().contains(p + "-click-actions")) {
                return;
            }

            clickActions.put(clickType,
                    new MenuClickActions(clickType, ListUtils.fromConfig(config, p + "-click-actions"))
            );
        }

    }

    public MenuItem() {}

    public MenuItem(@NotNull final MaterialBox materialBox) {
        super(materialBox);
    }

    public MenuItem(@NotNull final Material material) {
        super(material);
    }

    public MenuItem(@NotNull final String material) {
        super(material);
    }



    public MenuItem setID(@NotNull final String id) {
        this.id = id;
        return this;
    }

    public @NotNull String getID() {
        return id;
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

    public @NotNull List<Integer> getSlots() {
        if (slots == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(slots);
    }



    public MenuItem setViewRequirements(@Nullable final MenuRequirements viewRequirements) {
        this.viewRequirements = viewRequirements;
        return this;
    }

    public @Nullable MenuRequirements getViewRequirements() {
        return viewRequirements;
    }



    public MenuItem setClickRequirements(@Nullable final Map<MenuClickType, MenuClickRequirements> clickRequirements) {
        if (clickRequirements == null) {
            this.clickRequirements = null;
        }

        else {
            this.clickRequirements = new HashMap<>(clickRequirements);
        }

        return this;
    }

    public @Nullable MenuClickRequirements getClickRequirements(@NotNull final MenuClickType clickType) {
        return clickRequirements.getOrDefault(clickType, null);
    }



    public MenuItem setClickActions(@Nullable final Map<MenuClickType, MenuClickActions> clickActions) {
        if (clickActions == null) {
            this.clickActions = null;
        }

        else {
            this.clickActions = new HashMap<>(clickActions);
        }

        return this;
    }

    public @Nullable MenuClickActions getClickActions(@NotNull final MenuClickType clickType) {
        return clickActions.getOrDefault(clickType, null);
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
