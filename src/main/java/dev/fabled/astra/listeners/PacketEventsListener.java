package dev.fabled.astra.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import dev.fabled.astra.mines.generator.MineGenerator;
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

    private boolean rightClickPickaxeDetected = false;

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
                user.getUUID();
                Vector3i blockPosition = new Vector3i(blockX, blockY, blockZ);

                World world = Bukkit.getWorld("world");
                Block block = world.getBlockAt(blockX, blockY, blockZ);


                List<UUID> playerUUIDsForBlock = MineGenerator.getPlayerUUIDsForBlock(block, Bukkit.getPlayer(user.getUUID()).getUniqueId());
                boolean belongsToPlayer = playerUUIDsForBlock.contains(Bukkit.getPlayer(user.getUUID()).getUniqueId());
                if (!belongsToPlayer) {
                    return;
                }

                if (user != null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getGameMode().equals(GameMode.CREATIVE)) {
                            return;
                        }
                        if (Bukkit.getPlayer(user.getUUID()).getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                            rightClickPickaxeDetected = false;
                            WrapperPlayServerBlockChange blockChangePacket = new WrapperPlayServerBlockChange(blockPosition, 0);
                            PacketEvents.getAPI().getPlayerManager().sendPacket(Bukkit.getPlayer(user.getUUID()), blockChangePacket);
                            if (block.hasMetadata("material")) {
                                String material = block.getState().getMetadata("material").get(0).asString();
                                Material blockMaterial = Material.valueOf(material);
                                Bukkit.getPlayer(user.getUUID()).spawnParticle(Particle.BLOCK, block.getLocation().add(0.5, 0.5, 0.5), 75, 0.1, 0.1, 0.1, blockMaterial.createBlockData());
                                MineGenerator.removeBlockFromMap(block.getState(), Bukkit.getPlayer(user.getUUID()).getUniqueId());
                            }
                        }
                    }
                }

                copy.cleanUp();
            });
        }
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            PacketReceiveEvent copy = event.clone();
            EXECUTOR.execute(() -> {
                //WrapperPlayClientUseItem useItem = new WrapperPlayClientUseItem(copy);
                WrapperPlayClientPlayerBlockPlacement blockPlacement = new WrapperPlayClientPlayerBlockPlacement(copy);
                User user = copy.getUser();
                if (user != null) {
                    Player player = Bukkit.getPlayer(user.getUUID());
                    if (player != null) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            rightClickPickaxeDetected = true;
                        }
                    }
                }
                copy.cleanUp();
            });
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        Player player = Bukkit.getPlayer(user.getUUID());
        if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
            if (rightClickPickaxeDetected) {
                event.setCancelled(true);
            } else if (player.hasMetadata("drilling")) {
                rightClickPickaxeDetected = false;
            }

        }
    }

}

