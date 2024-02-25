package dev.paracausal.astra.menus;

import dev.paracausal.astra.utilities.MiniColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AstraMenu implements InventoryHolder {

    public static @NotNull AstraMenuBuilder builder() {
        return new AstraMenuBuilder();
    }

    private @NotNull final String title;
    private final int size;

    AstraMenu(@NotNull final String title, final int size) {
        this.title = title;
        this.size = size;
    }

    public AstraMenuBuilder toBuilder() {
        return new AstraMenuBuilder()
                .setTitle(title)
                .setSize(size);
    }

    public @NotNull Inventory getInventory(@Nullable OfflinePlayer player) {
        final Inventory inventory = Bukkit.createInventory(this, size, MiniColor.COLOR.deserialize(title));



        return inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return  getInventory(null);
    }

}
