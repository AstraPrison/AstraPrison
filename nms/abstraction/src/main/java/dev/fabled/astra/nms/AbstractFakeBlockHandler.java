package dev.fabled.astra.nms;

import dev.fabled.astra.nms.regions.AbstractRegion;
import dev.fabled.astra.utils.WeightedList;
import dev.fabled.astra.utils.blocks.BlockMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface AbstractFakeBlockHandler {

    void setBlock(
            final @NotNull Player player,
            final @NotNull BlockMaterial material,
            final @NotNull Location location
    );

    void fillRegion(
            final @NotNull Player player,
            final @NotNull BlockMaterial material,
            final @NotNull AbstractRegion region
    );

    void fillRegion(
            final @NotNull Player player,
            final @NotNull WeightedList<BlockMaterial> materials,
            final @NotNull AbstractRegion region
    );

}
