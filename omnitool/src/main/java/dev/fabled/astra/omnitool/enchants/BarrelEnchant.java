package dev.fabled.astra.omnitool.enchants;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.MineData;
import dev.fabled.astra.utils.MineReader;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.fabled.astra.utils.MineWriter.FILE;

public class BarrelEnchant {

    public static void trigger(Player player, Block block) {
        Location playerLocation = player.getLocation();
        String filePath = FILE;
        String mineName = block.getMetadata("mineName").get(0).asString();
        int removedBlockCount = 0;
        MineData mineData = MineReader.readMineData(filePath, mineName);
        Location startLocation = new Location(block.getWorld(), mineData.getStartX(), mineData.getStartY(), mineData.getStartZ());
        Location endLocation = new Location(block.getWorld(), mineData.getEndX(), mineData.getEndY(), mineData.getEndZ());
        int barrelCount = 8;
        int barrelRadius = 3;
        int explosionRadius = 5;

        int sizeX = endLocation.getBlockX() - startLocation.getBlockX() + 1;
        int sizeZ = endLocation.getBlockZ() - startLocation.getBlockZ() + 1;

        List<BlockState> barrelStates = new ArrayList<>();

        for (int i = 0; i < barrelCount; i++) {
            double angle = 2 * Math.PI / barrelCount * i;
            double x = playerLocation.getX() + barrelRadius * Math.cos(angle);
            double y = playerLocation.getY();
            double z = playerLocation.getZ() + barrelRadius * Math.sin(angle);

            if (x >= startLocation.getBlockX() && x <= endLocation.getBlockX() &&
                    z >= startLocation.getBlockZ() && z <= endLocation.getBlockZ()) {

                Location barrelLocation = new Location(playerLocation.getWorld(), x, y, z);
                Block barrelBlock = barrelLocation.getBlock();

                if (barrelBlock.getType() == Material.AIR) {
                    BlockState state = barrelBlock.getState();
                    state.setType(Material.BARREL);
                    player.sendTitle(ChatColor.YELLOW + "Barrel", ChatColor.WHITE + "explosion!", 20, 20, 20);
                    player.sendBlockChanges(Collections.singletonList(state));
                    barrelStates.add(state);
                    removedBlockCount++;
                    //removedBlocks.put(player.getUniqueId(), removedBlocks.getOrDefault(player.getUniqueId(), 0) + removedBlockCount);
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(Astra.getPlugin(), () -> {
            for (BlockState state : barrelStates) {
                state.setType(Material.AIR);
                state.update(true, false);
            }

            sendSphericalExplosion(player, playerLocation, explosionRadius, startLocation, endLocation);
        }, 100L);
    }

    private static void sendSphericalExplosion(Player player, Location center, int radius, Location startLocation, Location endLocation) {
        World world = center.getWorld();
        if (world == null) return;

        int minX = Math.min(startLocation.getBlockX(), endLocation.getBlockX());
        int minY = Math.min(startLocation.getBlockY(), endLocation.getBlockY());
        int minZ = Math.min(startLocation.getBlockZ(), endLocation.getBlockZ());
        int maxX = Math.max(startLocation.getBlockX(), endLocation.getBlockX());
        int maxY = Math.max(startLocation.getBlockY(), endLocation.getBlockY());
        int maxZ = Math.max(startLocation.getBlockZ(), endLocation.getBlockZ());

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radius * radius) {
                        Location explosionLocation = center.clone().add(x, y, z);
                        int blockX = explosionLocation.getBlockX();
                        int blockY = explosionLocation.getBlockY();
                        int blockZ = explosionLocation.getBlockZ();
                        if (blockX >= minX && blockX <= maxX &&
                                blockY >= minY && blockY <= maxY &&
                                blockZ >= minZ && blockZ <= maxZ) {
                            Block block = world.getBlockAt(explosionLocation);
                            player.sendBlockChange(explosionLocation, Material.AIR.createBlockData());
                            player.spawnParticle(Particle.EXPLOSION, explosionLocation, 1);
                        }
                    }
                }
            }
        }
    }
}
