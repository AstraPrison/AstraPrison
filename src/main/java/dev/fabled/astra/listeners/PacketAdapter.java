package dev.fabled.astra.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import dev.fabled.astra.packet.DrillPacketHandler;
import dev.fabled.astra.utils.PlayerDataWriter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PacketAdapter implements Listener {

    private static boolean listenersRegistered = false;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        String playerName = event.getPlayer().getName();
        UUID playerUUID = event.getPlayer().getUniqueId();


        if (!PlayerDataWriter.doesPlayerExist(playerUUID.toString())) {
            // Standardwerte setzen
            int defaultTokens = 0;
            double defaultMoney = 0.0;
            int defaultOmniToolLevel1 = 0;
            int defaultOmniToolLevel2 = 0;
            int defaultOmniToolLevel3 = 0;
            int defaultOmniToolLevel4 = 0;
            int defaultBrokenBlocks = 0;

            PlayerDataWriter.writePlayerToFile(
                    playerName, playerUUID,
                    defaultTokens, defaultMoney,
                    defaultOmniToolLevel1, defaultOmniToolLevel2,
                    defaultOmniToolLevel3, defaultOmniToolLevel4,
                    defaultBrokenBlocks
            );
        }
        if (!listenersRegistered) {
            PacketEvents.getAPI().getEventManager().registerListener(new DrillPacketHandler());
            listenersRegistered = true;
        }


    }
}
