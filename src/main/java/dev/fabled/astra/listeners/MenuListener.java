package dev.fabled.astra.listeners;

import dev.fabled.astra.menus.AstraMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.Set;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Inventory inventory = event.getClickedInventory();
        if (inventory == null || !(inventory.getHolder() instanceof AstraMenu menu)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof AstraMenu)) {
            return;
        }

        final int size = inventory.getSize();
        final Set<Integer> slots = event.getRawSlots();

        for (int slot = 0; slot < size; slot++) {
            if (slots.contains(slot)) {
                event.setCancelled(true);
                break;
            }
        }
    }

}
