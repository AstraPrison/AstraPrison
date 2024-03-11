package dev.paracausal.astra.listeners;

import dev.paracausal.astra.Astra;
import dev.paracausal.astra.api.actions.ActionManager;
import dev.paracausal.astra.menus.AstraMenu;
import dev.paracausal.astra.menus.items.MenuClickActions;
import dev.paracausal.astra.menus.items.MenuClickRequirements;
import dev.paracausal.astra.menus.items.MenuClickType;
import dev.paracausal.astra.menus.items.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public class MenuListener implements Listener {

    private final ActionManager actionManager;

    public MenuListener() {
        actionManager = Astra.getAPI().getActionManager();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        final InventoryHolder clickedHolder = clickedInventory.getHolder();
        if (!(clickedHolder instanceof AstraMenu menu)) {
            return;
        }

        event.setCancelled(true);

        final ClickType clickType = event.getClick();
        final MenuClickType menuClickType;
        try { menuClickType = MenuClickType.valueOf(clickType.toString()); }
        catch (IllegalArgumentException e) {
            return;
        }

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        final ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(Astra.getMenuItemNamespacedKey())) {
            return;
        }

        final String itemID = pdc.get(Astra.getMenuItemNamespacedKey(), PersistentDataType.STRING);
        if (itemID == null || itemID.length() < 1) {
            return;
        }

        final MenuItem menuItem = menu.getItem(itemID);
        if (menuItem == null) {
            return;
        }

        final Player player = (Player) event.getWhoClicked();

        final MenuClickRequirements requirements = menuItem.getClickRequirements(menuClickType);
        if (requirements != null && !requirements.check(player)) {
            return;
        }

        final MenuClickActions actions = menuItem.getClickActions(menuClickType);
        if (actions == null) {
            return;
        }

        actions.commandLines().forEach(commandLine -> actionManager.runCommandLine(player, commandLine));
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
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
