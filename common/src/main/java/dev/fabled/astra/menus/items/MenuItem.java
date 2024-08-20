package dev.fabled.astra.menus.items;

import dev.fabled.astra.api.actions.ActionArgument;
import dev.fabled.astra.exceptions.InvalidMaterialException;
import dev.fabled.astra.menus.items.actions.MenuItemActions;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.items.*;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuItem extends ItemBuilder {

    private @NotNull final String id;
    private @Nullable SlotSet slots;
    private @Nullable Map<MenuClickType, MenuItemActions> clickActions;

    public MenuItem(@NotNull final String id) {
        super();
        this.id = id;
    }

    public MenuItem(@NotNull final String id, @NotNull final MaterialBox materialBox) {
        super(materialBox);
        this.id = id;
    }

    public MenuItem(@NotNull final String id, @NotNull final Material material) {
        super(material);
        this.id = id;
    }

    public MenuItem(@NotNull final String id, @NotNull final String material) throws InvalidMaterialException {
        super(material);
        this.id = id;
    }

    public MenuItem(@NotNull final String id, @NotNull final YamlConfig config, @NotNull final String path) {
        super(config, path);
        this.id = id;
        this.slots = SlotUtils.parse(ListUtils.fromConfig(config, path + ".slots"));

        final ConfigurationSection clickActions = config.options().getConfigurationSection(path + ".click-actions");
        if (clickActions != null) {
            clickActions.getKeys(false).forEach(clickType -> {
                final MenuClickType menuClickType = MenuClickType.getFromString(clickType);
                if (menuClickType == null) {
                    return;
                }

                this.clickActions.put(menuClickType, new MenuItemActions(menuClickType, config, path + ".click-actions"));
            });
        }
    }

    public @NotNull String getId() {
        return id;
    }



    public @NotNull MenuItem setSlots(@Nullable final Set<Integer> slots) {
        this.slots = slots == null ? null : new SlotSet(slots);
        return this;
    }

    public @Nullable SlotSet getSlots() {
        return slots == null ? null : new SlotSet(slots);
    }



    public @NotNull MenuItem setClickActions(final @Nullable Map<MenuClickType, MenuItemActions> map) {
        if (map == null) {
            clickActions = null;
            return this;
        }

        this.clickActions = new HashMap<>(map);
        return this;
    }

    public @Nullable Map<MenuClickType, MenuItemActions> getClickActions() {
        if (clickActions == null) {
            return null;
        }

        return new HashMap<>(clickActions);
    }

    public @Nullable List<ActionArgument> getClickActions(final @NotNull MenuClickType clickType) {
        if (clickActions == null) {
            return null;
        }

        final MenuItemActions actions = clickActions.getOrDefault(clickType, null);
        return actions == null ? null : actions.getActions();
    }

    public @Nullable List<ActionArgument> getClickActions(final @NotNull ClickType clickType) {
        if (clickActions == null) {
            return null;
        }

        final MenuClickType menuClickType = MenuClickType.getFromClickType(clickType);
        final MenuItemActions actions = clickActions.getOrDefault(menuClickType, null);
        return actions == null ? null : actions.getActions();
    }

}
