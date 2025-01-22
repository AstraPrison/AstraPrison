package dev.fabled.astra.nms;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AbstractNMSHandler {

    AbstractFakeBlockHandler getFakeBlockHandler();
    AbstractPacketManager getPacketManager();
    AbstractPacketListener getPacketListener();

    void sendPacket(final @NotNull Player player, final @NotNull Object packet);

    boolean canBreakMaterial(
            final @NotNull Player player,
            final @Nullable ItemStack itemStack,
            final @NotNull Block block
    );

}
