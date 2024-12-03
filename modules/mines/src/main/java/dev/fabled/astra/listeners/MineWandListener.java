package dev.fabled.astra.listeners;

import dev.fabled.astra.mines.wand.MineWand;
import dev.fabled.astra.mines.wand.MineWandSelection;
import dev.fabled.astra.modules.impl.MinesModule;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MineWandListener implements AstraListener {

    private final @NotNull MineWandSelection selection;

    public MineWandListener() {
        selection = MinesModule.getInstance().getMineWandSelection();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final ItemStack item = event.getItem();
        if (!MineWand.getInstance().isMineWand(item)) {
            return;
        }

        final Player player = event.getPlayer();
        if (!player.hasPermission("astra.admin")) {
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        event.setCancelled(true);
        final Location location = block.getLocation();

        if (action.isLeftClick()) {
            if (!selection.setCornerOne(player, location)) {
                return;
            }

            sendUpdateMessage(player, location, true);
            return;
        }

        if (!selection.setCornerTwo(player, location)) {
            return;
        }

        sendUpdateMessage(player, location, false);
    }

    private void sendUpdateMessage(final @NotNull Player player, final @NotNull Location location, final boolean one) {
        final String corner = one ? "one" : "two";
        player.sendMessage(MiniColor.CHAT.deserialize(
                "<green>You set position " + corner + " to<white> "
                        + location.getBlockX() + ", "
                        + location.getBlockY() + ", "
                        + location.getBlockZ() + "<green>!"
        ));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        selection.loadPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        selection.savePlayer(player);
        selection.clearSelection(player);
    }

}
