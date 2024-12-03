package dev.fabled.astra.nms;

import dev.fabled.astra.nms.regions.AbstractRegion;
import dev.fabled.astra.utils.WeightedList;
import dev.fabled.astra.utils.blocks.BlockMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.Map;
import java.util.UUID;

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

    boolean isFakeBlock(final @NotNull UUID uuid, final int x, final int y, final int z);

    default boolean isFakeBlock(final @NotNull Player player, final int x, final int y, final int z) {
        return isFakeBlock(player.getUniqueId(), x, y, z);
    }

    default boolean isFakeBlock(final @NotNull UUID uuid, final @NotNull Location location) {
        return isFakeBlock(uuid, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    default boolean isFakeBlock(final @NotNull Player player, final @NotNull Location location) {
        return isFakeBlock(player.getUniqueId(), location);
    }

    @NotNull Map<Vector3i, Material> getFakeBlocks(final @NotNull UUID uuid);

    default @NotNull Map<Vector3i, Material> getFakeBlocks(final @NotNull Player player) {
        return getFakeBlocks(player.getUniqueId());
    }

    @Nullable
    Material getFakeBlock(final @NotNull UUID uuid, final int x, final int y, final int z);

    default @Nullable Material getFakeBlock(final @NotNull Player player, final int x, final int y, final int z) {
        return getFakeBlock(player.getUniqueId(), x, y, z);
    }

    default @Nullable Material getFakeBlock(final @NotNull UUID uuid, final @NotNull Location location) {
        return getFakeBlock(uuid, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    default @Nullable Material getFakeBlock(final @NotNull Player player, final @NotNull Location location) {
        return getFakeBlock(player.getUniqueId(), location);
    }

    void clear(final @NotNull UUID uuid);

    default void clear(final @NotNull Player player) {
        clear(player.getUniqueId());
    }

}
