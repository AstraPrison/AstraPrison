package dev.fabled.astra.listeners;

import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.omnitool.OmniTool;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class OmniToolLockListener implements AstraListener {

    private boolean isLocked;
    private boolean keepOnDeath;

    public OmniToolLockListener() {
        isLocked = true;
        keepOnDeath = true;
    }

    @Override
    public void onReload() {
        final YamlConfig config = OmniToolModule.getInstance().getOmniToolYml();
        isLocked = config.options().getBoolean("lock-omni-tool-to-inventory", true);
        keepOnDeath = config.options().getBoolean("keep-omni-tool-on-death", true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isLocked) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        final Inventory topInventory = player.getOpenInventory().getTopInventory();
        final int size = topInventory.getSize() - 1;

        final int slot = event.getRawSlot();
        final ClickType clickType = event.getClick();
        ItemStack item;

        if (slot < 0 || slot > size) {
            if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
                item = event.getCurrentItem();

                if (OmniTool.isOmniTool(item)) {
                    event.setCancelled(true);
                }
            }

            return;
        }

        final PlayerInventory inventory = player.getInventory();
        item = event.getCursor();

        if (clickType == ClickType.NUMBER_KEY) {
            item = inventory.getItem(event.getHotbarButton());
        }

        else if (clickType == ClickType.SWAP_OFFHAND) {
            item = inventory.getItemInOffHand();
        }

        if (OmniTool.isOmniTool(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!isLocked) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        final Inventory topInventory = player.getOpenInventory().getTopInventory();
        final int size = topInventory.getSize() - 1;

        final Set<Integer> slots = event.getRawSlots();
        boolean isInTopInventory = false;

        for (int i = 0; i < size; i++) {
            if (slots.contains(i)) {
                isInTopInventory = true;
                break;
            }
        }

        if (!isInTopInventory) {
            return;
        }

        final ItemStack item = event.getOldCursor();
        if (OmniTool.isOmniTool(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!isLocked) {
            return;
        }

        final ItemStack item = event.getItemDrop().getItemStack();
        if (OmniTool.isOmniTool(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!keepOnDeath) {
            return;
        }

        final List<ItemStack> drops = event.getDrops();
        final List<ItemStack> omniTools = new ArrayList<>();

        for (final ItemStack item : drops) {
            if (OmniTool.isOmniTool(item)) {
                omniTools.add(item);
            }
        }

        omniTools.forEach(drops::remove);
        omniTools.forEach(event.getItemsToKeep()::add);
    }

}
