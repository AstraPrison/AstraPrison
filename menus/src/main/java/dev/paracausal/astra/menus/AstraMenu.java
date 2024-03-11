package dev.paracausal.astra.menus;

import dev.paracausal.astra.Astra;
import dev.paracausal.astra.api.actions.ActionManager;
import dev.paracausal.astra.menus.items.MenuItem;
import dev.paracausal.astra.menus.items.MenuRequirement;
import dev.paracausal.astra.menus.items.MenuRequirements;
import dev.paracausal.astra.utilities.MiniColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class AstraMenu implements InventoryHolder {

    public static @NotNull AstraMenuBuilder builder() {
        return new AstraMenuBuilder();
    }

    private @NotNull final String title;
    private final int size;
    private final Map<String, MenuItem> items;

    AstraMenu(@NotNull final String title, final int size, @NotNull final Map<String, MenuItem> items) {
        this.title = title;
        this.size = size;
        this.items = items;
    }

    public AstraMenuBuilder toBuilder() {
        return new AstraMenuBuilder()
                .setTitle(title)
                .setSize(size)
                .setItems(items);
    }

    public @NotNull Inventory getInventory(@Nullable OfflinePlayer player) {
        final Inventory inventory = Bukkit.createInventory(this, size, MiniColor.COLOR.deserialize(title));

        items.forEach((id, item) -> {
            final MenuRequirements viewRequirements = item.getViewRequirements();
            if (viewRequirements != null && !viewRequirements.check(player)) {
                return;
            }

            final List<Integer> slots = item.getSlots();
            final ItemStack built = item.build(player);
            slots.forEach(slot -> inventory.setItem(slot, built));
        });

        return inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return  getInventory(null);
    }

    public @Nullable MenuItem getItem(@NotNull final String id) {
        return items.getOrDefault(id, null);
    }

}
