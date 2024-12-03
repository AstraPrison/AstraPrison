package dev.fabled.astra.nms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface AbstractPacket {

    /**
     * Run the code for this packet listener!
     * @param player The {@link Player} involved with the packet!
     * @param packet The {@link Object} (packet) in question!
     * @return
     * <br><code>true</code> to send the packet
     * <br><code>false</code> to prevent the packet from being sent
     */
    boolean run(final @NotNull Player player, final @NotNull Object packet);

    @NotNull Class<?> getPacketClass();

}
