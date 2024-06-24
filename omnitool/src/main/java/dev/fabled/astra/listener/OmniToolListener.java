package dev.fabled.astra.listener;

import dev.fabled.astra.omnitool.OmniToolItem;
import dev.fabled.astra.omnitool.menu.OmnitoolMenu;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OmniToolListener implements Listener {

    private final OmnitoolMenu omnitoolMenu = new OmnitoolMenu();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        if (OmniToolItem.isOmniTool(item) && OmniToolItem.isOwner(player, item)) {
            switch (event.getAction()) {
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    player.openInventory(omnitoolMenu.getInventory(player));
                    event.setCancelled(true);
                    break;
                default:
                    break;
            }
        } else if (OmniToolItem.isOmniTool(item) && !OmniToolItem.isOwner(player, item)) {
            player.sendMessage(MiniColor.parse("<red>-- This is not your tool! --"));
        }
    }
}
