package dev.fabled.astra.nms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface AbstractNMSHandler {

    void sendPacket(final @NotNull Player player, final @NotNull Object packet);

    @NotNull AbstractFakeBlockHandler getFakeBlockHandler();

}
