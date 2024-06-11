package dev.fabled.astra.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import dev.fabled.astra.Astra;
import dev.fabled.astra.listener.EnchantsTriggerEventListener;
import dev.fabled.astra.listener.VirtualBlockBreak;
import dev.fabled.astra.mines.generator.MineGenerator;
import dev.fabled.astra.omnitool.OmniToolItem;
import dev.fabled.astra.utils.MineData;
import dev.fabled.astra.utils.PlayerDataWriter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketEventsListener extends PacketListenerAbstract {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private boolean rightClickPickaxeDetected = true;
    private boolean luckyblockfound = false;

    public static final Map<UUID, Map<String, Integer>> playerBlockCounts = new ConcurrentHashMap<>();
    private static final int RESET_THRESHOLD_PERCENTAGE = 50;

    private final EnchantsTriggerEventListener enchantsTriggerEventListener;

    public PacketEventsListener() {
        super(PacketListenerPriority.NORMAL);
        this.enchantsTriggerEventListener = new EnchantsTriggerEventListener();
    }

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

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            PacketReceiveEvent copy = event.clone();
            EXECUTOR.execute(() -> {
                WrapperPlayClientPlayerDigging diggingWrapper = new WrapperPlayClientPlayerDigging(copy);
                int blockX = diggingWrapper.getBlockPosition().getX();
                int blockY = diggingWrapper.getBlockPosition().getY();
                int blockZ = diggingWrapper.getBlockPosition().getZ();
                //User user = copy.getUser();
                User user = event.getUser();

                if (user == null || user.getUUID() == null) {
                    return;
                }

                UUID userUUID = user.getUUID();
                Player player = Bukkit.getPlayer(userUUID);
                if (player == null) {
                    return;
                }

                Vector3i blockPosition = new Vector3i(blockX, blockY, blockZ);
                World world = Bukkit.getWorld("world");
                Block block = world.getBlockAt(diggingWrapper.getBlockPosition().x, diggingWrapper.getBlockPosition().y, diggingWrapper.getBlockPosition().z);
                JavaPlugin plugin = Astra.getPlugin();
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (!block.hasMetadata("mineName") || block.getMetadata("mineName").isEmpty()) {
                    return;
                }
                String mineName = block.getMetadata("mineName").get(0).asString();

                List<UUID> playerUUIDsForBlock = MineGenerator.getPlayerUUIDsForBlock(block, userUUID);
                boolean belongsToPlayer = playerUUIDsForBlock.contains(userUUID);
                if (!belongsToPlayer) {
                    return;
                }

                if (itemInHand.getType() == Material.DIAMOND_PICKAXE && !DiggingAction.CANCELLED_DIGGING.equals(diggingWrapper.getAction())) {
                    if (!OmniToolItem.isOwner(player, itemInHand)) {
                        player.sendMessage(ChatColor.RED + "You can only use your own Omnitool!");
                        return;
                    }


                    WrapperPlayServerBlockChange blockChangePacket = new WrapperPlayServerBlockChange(blockPosition, 0);
                    PacketEvents.getAPI().getPlayerManager().sendPacket(player, blockChangePacket);
                    if (block.hasMetadata("material")) {
                        String material = block.getState().getMetadata("material").get(0).asString();
                        if (material.equals(MineData.luckyblockMaterial().name()) && !DiggingAction.CANCELLED_DIGGING.equals(diggingWrapper.getAction())) {
                            player.sendMessage("Luckyblock found!");
                            //LUCKYBLOCK REWARDS TODO
                            luckyblockfound = true;
                        }
                        Material blockMaterial = Material.valueOf(material);
                        player.spawnParticle(Particle.BLOCK, block.getLocation().add(0.5, 0.5, 0.5), 25, 0.1, 0.1, 0.1, blockMaterial.createBlockData());
                        MineGenerator.removeBlockFromMap(block.getState(), userUUID);
                        rightClickPickaxeDetected = false;
                        luckyblockfound = false;

                        updateBlockCount(userUUID, mineName, player);
                        VirtualBlockBreak virtualBlockBreak = new VirtualBlockBreak(enchantsTriggerEventListener);
                        virtualBlockBreak.onBlockBreak(new BlockBreakEvent(block, player));

                        if (shouldResetMine(userUUID, mineName)) {
                            resetMine(player, mineName);
                        }
                    }
                }
                copy.cleanUp();
            });
        }

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            PacketReceiveEvent copy = event.clone();
            EXECUTOR.execute(() -> {
                WrapperPlayClientPlayerBlockPlacement blockPlacement = new WrapperPlayClientPlayerBlockPlacement(copy);
                User user = copy.getUser();
                if (user == null || user.getUUID() == null) {
                    return;
                }

                Player player = Bukkit.getPlayer(user.getUUID());
                if (player != null) {
                    rightClickPickaxeDetected = true;
                }
                copy.cleanUp();
            });
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        // Debugging: Temporarily remove any cancellation logic to test block changes
        /*
        User user = event.getUser();
        if (user == null || user.getUUID() == null) {
            return;
        }

        Player player = Bukkit.getPlayer(user.getUUID());
        if (player == null) {
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
            if (rightClickPickaxeDetected) {
                event.setCancelled(true);
            } else if (player.hasMetadata("drilling")) {
                rightClickPickaxeDetected = false;
            } else if (player.hasMetadata("bomb")) {
                rightClickPickaxeDetected = false;
            } else if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HOE) {
                rightClickPickaxeDetected = false;
            }
        }
        */
    }

    private boolean shouldResetMine(UUID playerUUID, String mineName) {
        int blocksMined = playerBlockCounts.getOrDefault(playerUUID, Collections.emptyMap()).getOrDefault(mineName, 0);
        int totalBlocks = MineGenerator.getTotalBlocks(mineName);
        int threshold = (int) (totalBlocks * (RESET_THRESHOLD_PERCENTAGE / 100.0));
        return blocksMined >= threshold;
    }

    private void resetMine(Player player, String mineName) {
        Collection<BlockState> blockChanges = MineGenerator.getBlocks(player.getUniqueId(), mineName);
        player.sendBlockChanges(blockChanges);
        player.sendMessage(ChatColor.RED + mineName + ChatColor.GREEN + " has been reset!");
        for (Map<String, Integer> mineBlockCounts : playerBlockCounts.values()) {
            mineBlockCounts.remove(mineName);
        }
    }
}
