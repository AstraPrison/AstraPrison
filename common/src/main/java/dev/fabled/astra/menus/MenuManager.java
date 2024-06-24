package dev.fabled.astra.menus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuManager implements InventoryHolder {

    private final Inventory inventory;

    public MenuManager(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}