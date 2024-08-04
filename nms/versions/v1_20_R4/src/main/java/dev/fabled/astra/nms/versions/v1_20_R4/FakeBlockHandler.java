package dev.fabled.astra.nms.versions.v1_20_R4;

import dev.fabled.astra.nms.AbstractFakeBlockHandler;
import dev.fabled.astra.utils.blocks.BlockPosition;
import dev.fabled.astra.nms.regions.ShapedRegion;
import dev.fabled.astra.utils.blocks.BlockMaterial;
import dev.fabled.astra.utils.WeightedList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FakeBlockHandler implements AbstractFakeBlockHandler {

    @Override
    public void setBlock(final @NotNull Player player, final @NotNull BlockMaterial material, final @NotNull Location location) {
        final BlockState blockState = ((CraftBlockData) material.getMaterial().createBlockData()).getState();
        final BlockPos blockPos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        ((CraftPlayer) player).getHandle().connection.sendPacket(new ClientboundBlockUpdatePacket(blockPos, blockState));
    }

    @Override
    public void fillRegion(final @NotNull Player player, final @NotNull BlockMaterial material, final @NotNull ShapedRegion region) {
        final Map<BlockPosition, BlockData> blockDataMap = new HashMap<>();
        final BlockData blockData = material.getMaterial().createBlockData();

        region.getLocations().forEach(loc ->
                blockDataMap.put(new BlockPosition(loc[0], loc[1], loc[2]), blockData)
        );

        player.sendMultiBlockChange(blockDataMap);
    }

    @Override
    public void fillRegion(final @NotNull Player player, final @NotNull WeightedList<BlockMaterial> materials, final @NotNull ShapedRegion region) {
        final Map<BlockPosition, BlockData> blockDataMap = new HashMap<>();
        if (materials.def() == null) {
            materials.def(new BlockMaterial(Material.STONE));
        }

        region.getLocations().forEach(loc -> {
            final BlockMaterial blockMaterial = materials.nextOrDef();
            final Material material = blockMaterial == null ? Material.STONE : blockMaterial.getMaterial();
            blockDataMap.put(new BlockPosition(loc[0], loc[1], loc[2]), material.createBlockData());
        });

        player.sendMultiBlockChange(blockDataMap);
    }

}
