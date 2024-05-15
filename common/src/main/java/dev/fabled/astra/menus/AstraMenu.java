package dev.fabled.astra.menus;

import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.PapiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AstraMenu implements InventoryHolder {

    private String title;
    private int size;

    public @NotNull Inventory getInventory(@Nullable final Player player) {
        final String title = PapiUtils.parse(player, this.title);
        return Bukkit.createInventory(this, size, MiniColor.INVENTORY.deserialize(title));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getInventory(null);
    }

}
