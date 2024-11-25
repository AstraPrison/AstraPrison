package dev.fabled.astra.listeners;

import dev.fabled.astra.omnitool.OmniTool;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OmniToolUpdateListener implements AstraListener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        final ItemStack item = event.getItem();
        if (!OmniTool.isOmniTool(item)) {
            return;
        }

        OmniTool.updateTool(item, block);
    }

}
