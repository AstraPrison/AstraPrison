package dev.fabled.astra.listeners;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketInteractListener extends PacketListenerAbstract implements Listener {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private final Plugin plugin;

    public PacketInteractListener(Plugin plugin) {
        super(PacketListenerPriority.NORMAL);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        if (user != null) {
            Player player = Bukkit.getPlayer(user.getUUID());
            if (player != null) {
                if (player.hasMetadata("drilling")) {
                    List<MetadataValue> metadataValues = player.getMetadata("drilling");
                    if (metadataValues != null && !metadataValues.isEmpty()) {
                        MetadataValue metadataValue = metadataValues.get(0);
                        if (metadataValue.asBoolean()) {
                        }
                    }
                }
            }
        }
    }
}
