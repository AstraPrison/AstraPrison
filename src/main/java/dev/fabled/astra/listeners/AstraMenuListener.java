package dev.fabled.astra.listeners;

import dev.fabled.astra.Astra;
import dev.fabled.astra.menus.AstraMenu;
import dev.fabled.astra.menus.MenuClickType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public class AstraMenuListener implements AstraListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        final Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (!(topInventory.getHolder() instanceof AstraMenu astraMenu)) {
            return;
        }

        final int size = topInventory.getSize() - 1;
        final int slot = event.getRawSlot();
        if (slot < 0 || slot > size) {
            return;
        }

        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        final ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        final String itemId = container.get(Astra.getAstraMenuItemKey(), PersistentDataType.STRING);
        if (itemId == null) {
            return;
        }

        if (!astraMenu.checkRequirements(player, itemId, MenuClickType.ANY)) {
            return;
        }

        astraMenu.runClickActions(player, itemId, MenuClickType.ANY);

        final ClickType clickType = event.getClick();
        final MenuClickType menuClickType = MenuClickType.fromClickType(clickType);

        if (!astraMenu.checkRequirements(player, itemId, menuClickType)) {
            return;
        }

        astraMenu.runClickActions(player, itemId, menuClickType);
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
