package dev.fabled.astra.nms.versions.v1_21_R1;

import dev.fabled.astra.nms.AbstractFakeBlockHandler;
import dev.fabled.astra.nms.regions.AbstractRegion;
import dev.fabled.astra.utils.WeightedList;
import dev.fabled.astra.utils.blocks.BlockMaterial;
import dev.fabled.astra.utils.blocks.BlockPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class FakeBlockHandler implements AbstractFakeBlockHandler {

    @Override
    public void setBlock(
            final @NotNull Player player,
            final @NotNull BlockMaterial material,
            final @NotNull Location location
    ) {
        final BlockPos blockPos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final BlockState blockState = ((CraftBlock) material.getMaterial().createBlockData()).getHandle().getBlockState(blockPos);
        ((CraftPlayer) player).getHandle().connection.sendPacket(new ClientboundBlockUpdatePacket(blockPos, blockState));
    }

    @Override
    public void fillRegion(
            final @NotNull Player player,
            final @NotNull BlockMaterial material,
            final @NotNull AbstractRegion region
    ) {
        final Map<BlockPosition, BlockData> blockDataMap = new HashMap<>();
        final BlockData blockData = material.getMaterial().createBlockData();

        region.getLocations().forEach(loc ->
                blockDataMap.put(new BlockPosition(loc[0], loc[1], loc[2]), blockData)
        );

        player.sendMultiBlockChange(blockDataMap);
    }

    @Override
    public void fillRegion(
            final @NotNull Player player,
            final @NotNull WeightedList<BlockMaterial> materials,
            final @NotNull AbstractRegion region
    ) {
        final Map<BlockPosition, BlockData> blockDataMap = new HashMap<>();

        region.getLocations().forEach(loc -> {
            final BlockMaterial material = materials.next();

            final Material mat = material == null
                    ? Material.STONE
                    : material.getMaterial();

            final BlockData blockData = mat.createBlockData();
            blockDataMap.put(new BlockPosition(loc[0], loc[1], loc[2]), blockData);
        });

        player.sendMultiBlockChange(blockDataMap);
    }

}
