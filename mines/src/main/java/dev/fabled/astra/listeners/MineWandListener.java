package dev.fabled.astra.listeners;

import dev.fabled.astra.mines.wand.MineWand;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MineWandListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission("astra.admin.minewand")) {
            return;
        }

        final ItemStack item = player.getInventory().getItemInMainHand();
        if (!MineWand.isWand(item)) {
            return;
        }

        event.setCancelled(true);
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        final Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK) {
            if (!MineWand.hasPositionOne(player)) {
                MineWand.setPositionOne(player, event.getClickedBlock().getLocation());
                player.sendMessage(MiniColor.parse("<green>You set position one!"));
            } else {
                player.sendMessage(MiniColor.parse("<red>Position one is already set!"));
            }
            return;
        }

        if (action == Action.RIGHT_CLICK_BLOCK && !event.getHand().name().contains("OFF_HAND")) {
            if (!MineWand.hasPositionTwo(player)) {
                MineWand.setPositionTwo(player, event.getClickedBlock().getLocation());
                player.sendMessage(MiniColor.parse("<green>You set position two!"));
            } else {
                //player.sendMessage(MiniColor.parse("<red>Position two is already set!"));
            }
        }
    }
}
