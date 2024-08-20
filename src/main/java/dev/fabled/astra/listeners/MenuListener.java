package dev.fabled.astra.listeners;

import dev.fabled.astra.AstraPrisonAPI;
import dev.fabled.astra.api.actions.ActionManager;
import dev.fabled.astra.api.actions.AstraAction;
import dev.fabled.astra.menus.AstraMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class MenuListener implements Listener {

    private final @Nullable ActionManager actionManager;

    public MenuListener() {
        final AstraPrisonAPI api = AstraPrisonAPI.get();
        actionManager = api == null ? null : api.getActionManager();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Inventory inventory = event.getClickedInventory();
        if (inventory == null || !(inventory.getHolder() instanceof AstraMenu menu)) {
            return;
        }

        event.setCancelled(true);
        if (actionManager == null) {
            return;
        }

        final int slot = event.getSlot();
        final Player player = (Player) event.getWhoClicked();

        menu.getClickActions(slot, event.getClick()).forEach(arg -> {
            final AstraAction action = actionManager.getAction(arg.getActionId());
            if (action == null) {
                return;
            }

            action.run(player, arg);
        });
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
