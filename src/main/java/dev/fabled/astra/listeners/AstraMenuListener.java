package dev.fabled.astra.listeners;

import dev.fabled.astra.menus.AstraMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.Set;

public class AstraMenuListener implements AstraListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        final Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (!(topInventory.getHolder() instanceof AstraMenu)) {
            return;
        }

        final int size = topInventory.getSize() - 1;
        final int slot = event.getRawSlot();

        if (slot < 0 || slot > size) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        final Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (!(topInventory.getHolder() instanceof AstraMenu)) {
            return;
        }

        final int size = topInventory.getSize() - 1;
        final Set<Integer> slots = event.getRawSlots();

        for (final int slot : slots) {
            if (slot < size) {
                event.setCancelled(true);
                return;
            }
        }
    }

}
