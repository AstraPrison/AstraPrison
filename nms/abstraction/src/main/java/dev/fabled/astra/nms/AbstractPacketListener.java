package dev.fabled.astra.nms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface AbstractPacketListener {

    void injectPlayer(final @NotNull Player player);
    void removePlayer(final @NotNull Player player);

}
