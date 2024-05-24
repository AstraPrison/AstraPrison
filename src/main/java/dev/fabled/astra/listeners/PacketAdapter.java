package dev.fabled.astra.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import dev.fabled.astra.Astra;
import dev.fabled.astra.specials.DrillPacketHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PacketAdapter implements Listener {

    private static boolean listenersRegistered = false;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!listenersRegistered) {
            PacketEvents.getAPI().getEventManager().registerListener(new PacketInteractListener(Astra.getPlugin()));
            PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener());
            PacketEvents.getAPI().getEventManager().registerListener(new DrillPacketHandler());
            listenersRegistered = true;
        }
    }
}
