package dev.fabled.astra.listeners;

//import com.github.retrooper.packetevents.PacketEvents;
//import com.github.retrooper.packetevents.event.PacketListenerAbstract;
//import com.github.retrooper.packetevents.event.PacketListenerPriority;
//import com.github.retrooper.packetevents.event.PacketReceiveEvent;
//import com.github.retrooper.packetevents.event.PacketSendEvent;
//import com.github.retrooper.packetevents.protocol.packettype.PacketType;
//import com.github.retrooper.packetevents.protocol.player.DiggingAction;
//import com.github.retrooper.packetevents.protocol.player.User;
//import com.github.retrooper.packetevents.util.Vector3i;
//import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
//import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
//import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import dev.fabled.astra.mines.generator.MineGenerator;
import dev.fabled.astra.omnitool.OmniToolItem;
import dev.fabled.astra.utils.*;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.*;

public class PacketEventsListener {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    private static final Map<Player, Integer> currentBlocksBroken = new HashMap<>();

    private boolean rightClickPickaxeDetected = true;
    private boolean luckyblockfound = false;

    public static final Map<UUID, Map<String, Integer>> playerBlockCounts = new ConcurrentHashMap<>();
    private static final int RESET_THRESHOLD_PERCENTAGE = 50;
    private static final Map<String, ScheduledFuture<?>> timedResetTasks = new ConcurrentHashMap<>();
    private static long RESET_DELAY = 40;

//    public PacketEventsListener() {
//        super(PacketListenerPriority.NORMAL);
//    }

    public static void updateBlockCount(UUID playerUUID, String mineName, Player player) {
        playerBlockCounts.putIfAbsent(playerUUID, new HashMap<>());
        Map<String, Integer> mineBlockCounts = playerBlockCounts.get(playerUUID);
        mineBlockCounts.put(mineName, mineBlockCounts.getOrDefault(mineName, 0) + 1);
        PlayerDataWriter.incrementBrokenBlocks(player.getUniqueId().toString());
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
        OmniToolItem.addBrokenBlock(itemInMainHand);
        OmniToolItem.updateLore(itemInMainHand, player.getUniqueId().toString());
        if (itemInOffHand.getType() == Material.DIAMOND_PICKAXE) {
            OmniToolItem.addBrokenBlock(itemInOffHand);
            OmniToolItem.updateLore(itemInOffHand, player.getUniqueId().toString());
        }
    }

    private static void resetMine(Player player, String mineName) {
        Collection<BlockState> blockChanges = MineGenerator.getBlocks(player.getUniqueId(), mineName);
        player.sendBlockChanges(blockChanges);
        player.sendMessage(MiniColor.parse("<red>" + mineName + " <green>has been reset!"));
        for (Map<String, Integer> mineBlockCounts : playerBlockCounts.values()) {
            mineBlockCounts.remove(mineName);
        }
        if (timedResetTasks.containsKey(mineName)) {
            timedResetTasks.get(mineName).cancel(false);
            timedResetTasks.remove(mineName);
        }
    }

    public static void scheduleTimedReset(Player player, String mineName, long delay, TimeUnit unit) {
        if (timedResetTasks.containsKey(mineName)) {
            timedResetTasks.get(mineName).cancel(false);
        }

        ScheduledFuture<?> resetTask = SCHEDULER.schedule(() -> {
            resetMine(player, mineName);
        }, delay, unit);

        timedResetTasks.put(mineName, resetTask);
    }

    private boolean shouldResetMine(UUID playerUUID, String mineName) {
        int blocksMined = playerBlockCounts.getOrDefault(playerUUID, Collections.emptyMap()).getOrDefault(mineName, 0);
        int totalBlocks = MineGenerator.getTotalBlocks(mineName);
        int threshold = (int) (totalBlocks * (RESET_THRESHOLD_PERCENTAGE / 100.0));
        return blocksMined >= threshold;
    }

//    @Override
//    public void onPacketReceive(PacketReceiveEvent event) {
//        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
//            PacketReceiveEvent copy = event.clone();
//            EXECUTOR.execute(() -> {
//                WrapperPlayClientPlayerDigging diggingWrapper = new WrapperPlayClientPlayerDigging(copy);
//                int blockX = diggingWrapper.getBlockPosition().getX();
//                int blockY = diggingWrapper.getBlockPosition().getY();
//                int blockZ = diggingWrapper.getBlockPosition().getZ();
//                User user = event.getUser();
//                if (user == null || user.getUUID() == null) {
//                    return;
//                }
//                UUID userUUID = user.getUUID();
//                Player player = Bukkit.getPlayer(userUUID);
//                if (player == null) {
//                    return;
//                }
//                Vector3i blockPosition = new Vector3i(blockX, blockY, blockZ);
//                World world = Bukkit.getWorld("world");
//                Block block = world.getBlockAt(diggingWrapper.getBlockPosition().x, diggingWrapper.getBlockPosition().y, diggingWrapper.getBlockPosition().z);
//                JavaPlugin plugin = Astra.getPlugin();
//                ItemStack itemInHand = player.getInventory().getItemInMainHand();
//                if (!block.hasMetadata("mineName") || block.getMetadata("mineName").isEmpty()) {
//                    return;
//                }
//                String mineName = block.getMetadata("mineName").get(0).asString();
//                List<UUID> playerUUIDsForBlock = MineGenerator.getPlayerUUIDsForBlock(block, userUUID);
//                boolean belongsToPlayer = playerUUIDsForBlock.contains(userUUID);
//                if (!belongsToPlayer) {
//                    return;
//                }
//                if (itemInHand.getType() == Material.DIAMOND_PICKAXE && !DiggingAction.CANCELLED_DIGGING.equals(diggingWrapper.getAction())) {
//                    if (!OmniToolItem.isOwner(player, itemInHand)) {
//                        player.sendMessage(MiniColor.parse("<red>You can only use your own Omnitool!"));
//                        return;
//                    }
//                    WrapperPlayServerBlockChange blockChangePacket = new WrapperPlayServerBlockChange(blockPosition, 0);
//                    PacketEvents.getAPI().getPlayerManager().sendPacket(player, blockChangePacket);
//                    if (block.hasMetadata("material")) {
//                        String material = block.getState().getMetadata("material").get(0).asString();
//                        if (material.equals(MineData.luckyblockMaterial().name()) && !DiggingAction.CANCELLED_DIGGING.equals(diggingWrapper.getAction())) {
//                            LuckyBlockReward.giveRandomLuckyBlockReward(player);
//                            luckyblockfound = true;
//                        }
//                        Material blockMaterial = Material.valueOf(material);
//                        player.spawnParticle(Particle.BLOCK, block.getLocation().add(0.5, 0.5, 0.5), 25, 0.1, 0.1, 0.1, blockMaterial.createBlockData());
//                        MineGenerator.removeBlockFromMap(block.getState(), userUUID);
//                        rightClickPickaxeDetected = false;
//                        luckyblockfound = false;
//                        updateBlockCount(userUUID, mineName, player);
//                        updatePickaxeLevel(player, userUUID.toString(), 1);
//                        EnchantsTriggerEventListener.getInstance().onBlockBreak(new BlockBreakEvent(block, player));
//                        if (MineReader.readMineData("plugins/Astra/data/mines.json", mineName).getresetType().equals("Blocks")) {
//                            if (shouldResetMine(userUUID, mineName)) {
//                                resetMine(player, mineName);
//                            }
//                        }
//                        if (MineReader.readMineData("plugins/Astra/data/mines.json", mineName).getresetType().equals("Timed")) {
//                            RESET_DELAY = MineReader.readMineData("plugins/Astra/data/mines.json", mineName).resetTime;
//                            scheduleTimedReset(player, mineName, RESET_DELAY, TimeUnit.SECONDS);
//                        }
//                        //scheduleTimedReset(player, mineName, RESET_DELAY, TimeUnit.MINUTES);
//                    }
//                }
//                copy.cleanUp();
//            });
//        }
//
//        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
//            PacketReceiveEvent copy = event.clone();
//            EXECUTOR.execute(() -> {
//                WrapperPlayClientPlayerBlockPlacement blockPlacement = new WrapperPlayClientPlayerBlockPlacement(copy);
//                User user = copy.getUser();
//                if (user == null || user.getUUID() == null) {
//                    return;
//                }
//
//                Player player = Bukkit.getPlayer(user.getUUID());
//                if (player != null) {
//                    rightClickPickaxeDetected = true;
//                }
//                copy.cleanUp();
//            });
//        }
//    }

//    @Override
//    public void onPacketSend(PacketSendEvent event) {
//
//        User user = event.getUser();
//        if (user == null || user.getUUID() == null) {
//            return;
//        }
//
//        Player player = Bukkit.getPlayer(user.getUUID());
//        if (player == null) {
//            return;
//        }
//
//        if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
//            if (rightClickPickaxeDetected) {
////                event.setCancelled(true);
//                player.sendMessage("Cancelled Packet!");
//            } else if (player.hasMetadata("drilling")) {
//                rightClickPickaxeDetected = false;
//            } else if (player.hasMetadata("bomb")) {
//                rightClickPickaxeDetected = false;
//            } else if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HOE) {
//                rightClickPickaxeDetected = false;
//            }
//        }
//    }

    private void updatePickaxeLevel(Player player, String playerUUID, int newBlocksBroken) {
        ItemStack pickaxe = player.getInventory().getItemInMainHand();
        if (pickaxe.getType() != Material.DIAMOND_PICKAXE || !OmniToolItem.isOmniTool(pickaxe)) {
            return;
        }

        ItemMeta meta = pickaxe.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        int currentPickaxeLevel = pdc.getOrDefault(OmniToolItem.LEVEL_KEY, PersistentDataType.INTEGER, 0);
        int startingCost = 100;
        int increaseBy = 400;

        int playerblocks = pdc.getOrDefault(OmniToolItem.BLOCKS_KEY, PersistentDataType.INTEGER, 0);
        int levelBlocks = pdc.getOrDefault(OmniToolItem.LEVEL_BLOCKS_KEY, PersistentDataType.INTEGER, 0);
        levelBlocks += newBlocksBroken;
        pdc.set(OmniToolItem.LEVEL_BLOCKS_KEY, PersistentDataType.INTEGER, levelBlocks);

        int requiredBlocksForNextLevel = startingCost + (currentPickaxeLevel * increaseBy);

        updateXPBar(player, levelBlocks, requiredBlocksForNextLevel);

        if (levelBlocks >= requiredBlocksForNextLevel) {
            int newPickaxeLevel = currentPickaxeLevel + 1;
            pdc.set(OmniToolItem.LEVEL_KEY, PersistentDataType.INTEGER, newPickaxeLevel);
            pdc.set(OmniToolItem.LEVEL_BLOCKS_KEY, PersistentDataType.INTEGER, levelBlocks - requiredBlocksForNextLevel);
            OmniToolItem.updateLore(pickaxe, playerUUID);

            player.sendMessage(MiniColor.parse("<green>Your Omnitool level has increased to </green>" + "<red>" + newPickaxeLevel));
            player.setExp(0);

            int xpLevelsToAdd = newPickaxeLevel - currentPickaxeLevel;
            for (int i = 0; i < xpLevelsToAdd; i++) {
                player.giveExpLevels(1);
            }
        }

        pickaxe.setItemMeta(meta);
    }

    private void updateXPBar(Player player, int levelBlocks, int requiredBlocksForNextLevel) {
        double progressPercentage = ((double) levelBlocks) / requiredBlocksForNextLevel;
        progressPercentage = Math.max(0.0, Math.min(1.0, progressPercentage));
        player.setExp((float) progressPercentage);
    }
}