package dev.fabled.astra.omnitool.actions;

import dev.fabled.astra.Astra;
import dev.fabled.astra.listener.EnchantsTriggerEventListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class changelayer {

    public changelayer(Player player, Block block, String action) {

        World world = player.getWorld();
        ArrayList<BlockState> removedBlocks = new ArrayList<>();

        int radius = 100;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location currentLocation = block.getLocation().clone().add(x, 0, z);
                Block currentBlock = world.getBlockAt(currentLocation);
                if (EnchantsTriggerEventListener.isPlayerMap(player, currentBlock) && currentBlock.hasMetadata("material")) {
                    Material BlockMaterial = action.contains("changelayer") ? Material.valueOf(action.split(" ")[1]) : Material.GOLD_BLOCK;
                    currentBlock.removeMetadata("material", Astra.getPlugin());
                    currentBlock.setMetadata("material", new FixedMetadataValue(Astra.getPlugin(), BlockMaterial.name()));
                    player.sendBlockChange(currentLocation, BlockMaterial.createBlockData());
                    removedBlocks.add(currentBlock.getState());
                }
            }
        }
    }
}
