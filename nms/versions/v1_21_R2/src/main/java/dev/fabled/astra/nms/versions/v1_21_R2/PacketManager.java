package dev.fabled.astra.nms.versions.v1_21_R2;

import dev.fabled.astra.nms.AbstractPacket;
import dev.fabled.astra.nms.AbstractPacketManager;
import dev.fabled.astra.nms.versions.v1_21_R2.impl.FakeBlockInteractPacket;
import dev.fabled.astra.nms.versions.v1_21_R2.impl.FakeBlockMinePacket;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PacketManager implements AbstractPacketManager {

    private final Map<Class<?>, List<AbstractPacket>> serverboundPackets;
    private final Map<Class<?>, List<AbstractPacket>> clientboundPackets;

    public PacketManager(final @NotNull JavaPlugin plugin) {
        serverboundPackets = new HashMap<>();
        clientboundPackets = new HashMap<>();

        addServerboundPacket(new FakeBlockMinePacket(plugin));
        addClientboundPacket(new FakeBlockInteractPacket());
    }

    @Override
    public @NotNull List<AbstractPacket> getServerboundPackets(final @NotNull Class<?> packetClass) {
        if (!serverboundPackets.containsKey(packetClass)) {
            return new ArrayList<>();
        }

        return serverboundPackets.get(packetClass);
    }

    @Override
    public @NotNull List<AbstractPacket> getClientboundPackets(final @NotNull Class<?> packetClass) {
        if (!clientboundPackets.containsKey(packetClass)) {
            return new ArrayList<>();
        }

        return clientboundPackets.get(packetClass);
    }

    @Override
    public void addServerboundPacket(final @NotNull AbstractPacket packet) {
        final Class<?> packetClass = packet.getPacketClass();
        if (!serverboundPackets.containsKey(packetClass)) {
            serverboundPackets.put(packetClass, new ArrayList<>(List.of(packet)));
            return;
        }

        serverboundPackets.get(packetClass).add(packet);
    }

    @Override
    public void addClientboundPacket(final @NotNull AbstractPacket packet) {
        final Class<?> packetClass = packet.getPacketClass();
        if (!clientboundPackets.containsKey(packetClass)) {
            clientboundPackets.put(packetClass, new ArrayList<>(List.of(packet)));
            return;
        }

        clientboundPackets.get(packetClass).add(packet);
    }

}
