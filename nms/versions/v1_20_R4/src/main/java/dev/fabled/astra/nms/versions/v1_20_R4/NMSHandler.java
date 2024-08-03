package dev.fabled.astra.nms.versions.v1_20_R4;

import dev.fabled.astra.nms.AbstractNMSHandler;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NMSHandler implements AbstractNMSHandler {

    @Override
    public void sendPacket(final @NotNull Player player, final @NotNull Object packet) {
        if (!(packet instanceof Packet<?> p)) {
            return;
        }

        ((CraftPlayer) player).getHandle().connection.send(p);
    }

}
