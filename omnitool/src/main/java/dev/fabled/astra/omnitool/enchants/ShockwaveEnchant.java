package dev.fabled.astra.omnitool.enchants;

import dev.fabled.astra.mines.generator.MineGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.fabled.astra.utils.MineWriter.FILE;

public class ShockwaveEnchant {

    public static void trigger(Player player, Block block, String mineName) {
        World world = player.getWorld();
        String filePath = FILE;
        ArrayList<BlockState> removedBlocks = new ArrayList<>();

        int radius = 50;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location currentLocation = block.getLocation().clone().add(x, 0, z);
                Block currentBlock = world.getBlockAt(currentLocation);
                if (isPlayerMap(player, currentBlock) && currentBlock.hasMetadata("material")) {
                    player.sendBlockChange(currentLocation, Material.AIR.createBlockData());
                    removedBlocks.add(currentBlock.getState());
                }
            }
        }
    }

    private static boolean isPlayerMap(Player player, Block block) {
        List<UUID> playerUUIDsForBlock = MineGenerator.getPlayerUUIDsForBlock(block, player.getUniqueId());
        return playerUUIDsForBlock.contains(player.getUniqueId());
    }
}
