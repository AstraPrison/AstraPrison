package dev.fabled.astra.nms;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AbstractOmniToolUpdater {

    void update(
            final @NotNull Player player,
            final @NotNull ItemStack itemStack,
            final @NotNull BlockState blockState
    );

}
