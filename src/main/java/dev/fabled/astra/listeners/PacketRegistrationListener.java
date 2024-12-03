package dev.fabled.astra.listeners;

import dev.fabled.astra.nms.AbstractFakeBlockHandler;
import dev.fabled.astra.nms.AbstractPacketListener;
import dev.fabled.astra.nms.NMSFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PacketRegistrationListener implements AstraListener {

    private final @NotNull AbstractPacketListener packetListener;
    private final @NotNull AbstractFakeBlockHandler fakeBlockHandler;

    public PacketRegistrationListener() {
        packetListener = NMSFactory.getNMSHandler().getPacketListener();
        fakeBlockHandler = NMSFactory.getNMSHandler().getFakeBlockHandler();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        packetListener.injectPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        packetListener.removePlayer(player);
        fakeBlockHandler.clear(player);
    }

}
