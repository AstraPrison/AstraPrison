package dev.fabled.astra.nms;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AbstractPacketManager {

    @NotNull List<AbstractPacket> getServerboundPackets(final Class<?> packetClass);
    @NotNull List<AbstractPacket> getClientboundPackets(final Class<?> packetClass);

    void addServerboundPacket(final @NotNull AbstractPacket packet);
    void addClientboundPacket(final @NotNull AbstractPacket packet);

}
