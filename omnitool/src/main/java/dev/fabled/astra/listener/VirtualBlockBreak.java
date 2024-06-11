package dev.fabled.astra.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class VirtualBlockBreak implements Listener {

    private final EnchantsTriggerEventListener enchantsTriggerEventListener;

    public VirtualBlockBreak(EnchantsTriggerEventListener enchantsTriggerEventListener) {
        this.enchantsTriggerEventListener = enchantsTriggerEventListener;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        String mineName = "exampleMine";


        enchantsTriggerEventListener.onBlockBreak(event);
    }
}
