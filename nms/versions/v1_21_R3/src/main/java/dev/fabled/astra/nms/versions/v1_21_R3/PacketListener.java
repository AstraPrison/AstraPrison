package dev.fabled.astra.nms.versions.v1_21_R3;

import dev.fabled.astra.nms.AbstractPacket;
import dev.fabled.astra.nms.AbstractPacketListener;
import io.netty.channel.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PacketListener implements AbstractPacketListener {

    private final @NotNull PacketManager packetManager;

    public PacketListener(final @NotNull PacketManager packetManager) {
        this.packetManager = packetManager;
    }

    @Override
    public void injectPlayer(final @NotNull Player player) {
        final ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            // From player to server
            @Override
            public void channelRead(final @NotNull ChannelHandlerContext context, final @NotNull Object packet) throws Exception {
                final List<AbstractPacket> listeningFor = packetManager.getServerboundPackets(packet.getClass());
                if (listeningFor.isEmpty()) {
                    super.channelRead(context, packet);
                    return;
                }

                for (final @NotNull AbstractPacket p : listeningFor) {
                    if (!p.run(player, packet)) {
                        continue;
                    }

                    super.channelRead(context, packet);
                }
            }

            // From server to player
            @Override
            public void write(final @NotNull ChannelHandlerContext context, final @NotNull Object packet, final @NotNull ChannelPromise promise) throws Exception {
                final List<AbstractPacket> listeningFor = packetManager.getClientboundPackets(packet.getClass());
                if (listeningFor.isEmpty()) {
                    super.write(context, packet, promise);
                    return;
                }

                for (final AbstractPacket p : listeningFor) {
                    if (!p.run(player, packet)) {
                        continue;
                    }

                    super.write(context, packet, promise);
                }
            }
        };

        final ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        pipeline.addBefore("packet_handler", "astra", channelDuplexHandler);
    }

    @Override
    public void removePlayer(final @NotNull Player player) {
        final Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

}
