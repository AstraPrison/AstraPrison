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
import dev.fabled.astra.mines.generator.MineGenerator;
import dev.fabled.astra.utils.MineData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketEventsListener extends PacketListenerAbstract {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private boolean rightClickPickaxeDetected = true;

    private boolean luckyblockfound = false;

    public PacketEventsListener() {
        super(PacketListenerPriority.NORMAL);
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
                User user = copy.getUser();

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
                Block block = world.getBlockAt(blockX, blockY, blockZ);

                List<UUID> playerUUIDsForBlock = MineGenerator.getPlayerUUIDsForBlock(block, userUUID);
                boolean belongsToPlayer = playerUUIDsForBlock.contains(userUUID);
                if (!belongsToPlayer) {
                    return;
                }

                if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                    WrapperPlayServerBlockChange blockChangePacket = new WrapperPlayServerBlockChange(blockPosition, 0);
                    PacketEvents.getAPI().getPlayerManager().sendPacket(player, blockChangePacket);
                    if (block.hasMetadata("material")) {
                        String material = block.getState().getMetadata("material").get(0).asString();
                        if (material.equals(MineData.luckyblockMaterial().name()) && !DiggingAction.CANCELLED_DIGGING.equals(diggingWrapper.getAction())) {
                            player.sendMessage("Luckyblock found!");
                            luckyblockfound = true;
                        }
                        Material blockMaterial = Material.valueOf(material);
                        player.spawnParticle(Particle.BLOCK, block.getLocation().add(0.5, 0.5, 0.5), 50, 0.1, 0.1, 0.1, blockMaterial.createBlockData());
                        MineGenerator.removeBlockFromMap(block.getState(), userUUID);
                        rightClickPickaxeDetected = false;
                        luckyblockfound = false;
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
            }
        }
    }


}

