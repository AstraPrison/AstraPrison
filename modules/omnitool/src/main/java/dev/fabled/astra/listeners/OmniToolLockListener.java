package dev.fabled.astra.listeners;

import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.omnitool.OmniTool;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

public class OmniToolLockListener implements AstraListener {

    private boolean isLocked;

    public OmniToolLockListener() {
        isLocked = true;
    }

    @Override
    public void onReload() {
        final YamlConfig config = OmniToolModule.getInstance().getOmniToolYml();
        isLocked = config.options().getBoolean("lock-omni-tool-to-inventory", true);
    }

//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent event) {
//        final ItemStack item = event.getCurrentItem();
//
//    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (!isLocked) {
            return;
        }

        final ItemStack item = event.getItem();
        if (OmniTool.isOmniTool(item)) {
            event.setCancelled(true);
        }
    }

}
