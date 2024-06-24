package dev.fabled.astra.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerProgressTracker {
    private static final Map<Player, Integer> currentBlocksBroken = new HashMap<>();
    private static final Map<Player, Integer> requiredBlocksForNextLevel = new HashMap<>();

    public static void initializePlayer(Player player, int startingCost, int increaseBy) {
        currentBlocksBroken.put(player, 0);
        requiredBlocksForNextLevel.put(player, startingCost);
    }

    public static void incrementBlocksBroken(Player player) {
        int currentBlocks = currentBlocksBroken.getOrDefault(player, 0);
        currentBlocksBroken.put(player, currentBlocks + 1);
    }

    public static int getCurrentBlocksBroken(Player player) {
        return currentBlocksBroken.getOrDefault(player, 0);
    }

    public static int getRequiredBlocksForNextLevel(Player player) {
        return requiredBlocksForNextLevel.getOrDefault(player, 0);
    }

    public static void updateRequiredBlocksForNextLevel(Player player, int increaseBy) {
        int currentRequiredBlocks = requiredBlocksForNextLevel.getOrDefault(player, 0);
        requiredBlocksForNextLevel.put(player, currentRequiredBlocks + increaseBy);
    }

    public static void resetPlayerProgress(Player player) {
        currentBlocksBroken.remove(player);
        requiredBlocksForNextLevel.remove(player);
    }
}