package dev.fabled.astra.utils;

import dev.fabled.astra.mines.generator.MineGenerator;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExplosiveReset {

    private static final double RESET_THRESHOLD_PERCENTAGE = 50.0;
    private static final Map<UUID, Map<String, Integer>> explosiveBlockCounts = new ConcurrentHashMap<>();

    public static void updateBlockCount(UUID playerUUID, String mineName) {
        explosiveBlockCounts.putIfAbsent(playerUUID, new HashMap<>());
        Map<String, Integer> mineBlockCounts = explosiveBlockCounts.get(playerUUID);
        mineBlockCounts.put(mineName, mineBlockCounts.getOrDefault(mineName, 0) + 1);

    }

    public static boolean shouldResetMine(UUID playerUUID, String mineName) {
        int blocksMined = explosiveBlockCounts.getOrDefault(playerUUID, Collections.emptyMap()).getOrDefault(mineName, 0);
        int totalBlocks = MineGenerator.getTotalBlocks(mineName);
        int threshold = (int) (totalBlocks * (RESET_THRESHOLD_PERCENTAGE / 100.0));
        return blocksMined >= threshold;
    }

    public static void resetMine(Player player, String mineName) {
        Collection<BlockState> blockChanges = MineGenerator.getBlocks(player.getUniqueId(), mineName);
        player.sendBlockChanges(blockChanges);
        player.sendMessage(ChatColor.RED + mineName + ChatColor.GREEN + " has been reset!");
        for (Map<String, Integer> mineBlockCounts : explosiveBlockCounts.values()) {
            mineBlockCounts.remove(mineName);
        }
    }
}
