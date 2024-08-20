package dev.fabled.astra.menus;

import dev.fabled.astra.api.actions.ActionArgument;
import dev.fabled.astra.menus.items.MenuClickType;
import dev.fabled.astra.menus.items.MenuItem;
import dev.fabled.astra.utils.MenuUtils;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import dev.fabled.astra.utils.items.SlotSet;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AstraMenu implements InventoryHolder {

    final @NotNull String id;
    private final @NotNull String title;
    private final int size;

    private final @NotNull Map<String, MenuItem> itemsById;
    private final @NotNull Map<Integer, String> itemIdsBySlot;

    public AstraMenu(final @NotNull String id, final @NotNull YamlConfig config) {
        this.id = id;
        title = config.options().getString("title", "Astra Menu");
        size = MenuUtils.convertToMenuSize(config.options().getInt("size", 54));
        itemsById = new HashMap<>();
        itemIdsBySlot = new HashMap<>();

        final ConfigurationSection contents = config.options().getConfigurationSection("contents");
        if (contents == null) {
            return;
        }

        contents.getKeys(false).forEach(itemId -> {
            final MenuItem menuItem = new MenuItem(itemId, config, "contents." + itemId);
            itemsById.put(itemId, menuItem);

            if (menuItem.getSlots() == null) {
                return;
            }

            menuItem.getSlots().forEach(slot ->
                itemIdsBySlot.put(slot, itemId)
            );
        });
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull Inventory getInventory(@Nullable final Player player) {
        final String title = PapiUtils.parse(player, this.title);
        final Inventory inventory = Bukkit.createInventory(this, size, MiniColor.INVENTORY.deserialize(title));

        itemsById.forEach((id, item) -> {
            final SlotSet slotSet = item.getSlots();
            if (slotSet == null) {
                return;
            }

            final ItemStack itemStack = item.build(player);
            slotSet.forEach(slot -> inventory.setItem(slot, itemStack));
        });

        return inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getInventory(null);
    }



    @Contract("_, _, !null -> !null")
    public @Nullable List<ActionArgument> getClickActions(
            final int slot,
            final @NotNull MenuClickType clickType,
            final @Nullable List<ActionArgument> def
    ) {
        final String id = itemIdsBySlot.getOrDefault(slot, null);
        if (id == null) {
            return def;
        }

        final MenuItem menuItem = itemsById.getOrDefault(id, null);
        if (menuItem == null) {
            return def;
        }

        return menuItem.getClickActions(clickType);
    }

    public @NotNull List<ActionArgument> getClickActions(final int slot, final @NotNull MenuClickType clickType) {
        return getClickActions(slot, clickType, new ArrayList<>());
    }



    @Contract("_, _, !null -> !null")
    public @Nullable List<ActionArgument> getClickActions(
            final int slot,
            final @NotNull ClickType clickType,
            final @Nullable List<ActionArgument> def
    ) {
        return getClickActions(slot, MenuClickType.getFromClickType(clickType), def);
    }

    public @NotNull List<ActionArgument> getClickActions(final int slot, final @NotNull ClickType clickType) {
        return getClickActions(slot, clickType, new ArrayList<>());
    }

}
