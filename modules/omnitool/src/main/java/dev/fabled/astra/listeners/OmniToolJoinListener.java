package dev.fabled.astra.listeners;

import dev.fabled.astra.omnitool.OmniTool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class OmniToolJoinListener implements AstraListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (OmniTool.hasOmniTool(player)) {
            return;
        }

        player.getInventory().addItem(OmniTool.getDefaultOmniTool());
    }

}
