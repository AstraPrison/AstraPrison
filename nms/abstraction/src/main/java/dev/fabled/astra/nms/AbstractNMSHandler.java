package dev.fabled.astra.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AbstractNMSHandler {

    void sendPacket(final @NotNull Player player, final @NotNull Object packet);

    boolean canBreakMaterial(final @NotNull ItemStack itemStack, final @NotNull Material material);

}
