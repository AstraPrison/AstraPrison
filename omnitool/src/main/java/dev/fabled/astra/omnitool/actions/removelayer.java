package dev.fabled.astra.omnitool.actions;

import dev.fabled.astra.listener.EnchantsTriggerEventListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class removelayer {

    public removelayer(Player player, Block block) {

        World world = player.getWorld();
        ArrayList<BlockState> removedBlocks = new ArrayList<>();

        int radius = 100;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location currentLocation = block.getLocation().clone().add(x, 0, z);
                Block currentBlock = world.getBlockAt(currentLocation);
                if (EnchantsTriggerEventListener.isPlayerMap(player, currentBlock) && currentBlock.hasMetadata("material")) {
                    player.sendBlockChange(currentLocation, Material.AIR.createBlockData());
                    removedBlocks.add(currentBlock.getState());
                }
            }
        }
    }
}
