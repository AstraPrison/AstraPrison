package dev.fabled.astra.specials;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import dev.fabled.astra.Astra;
import dev.fabled.astra.mines.generator.MineGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DrillPacketHandler extends PacketListenerAbstract {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Map<UUID, Integer> activeDrills = new HashMap<>();

    public DrillPacketHandler() {
        super(PacketListenerPriority.NORMAL);
    }

    public static int getActiveDrills(UUID playerUUID) {
        return activeDrills.getOrDefault(playerUUID, 0);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            PacketReceiveEvent copy = event.clone();
            EXECUTOR.execute(() -> {
                WrapperPlayClientPlayerBlockPlacement drillingWrapper = new WrapperPlayClientPlayerBlockPlacement(copy);
                int blockX = drillingWrapper.getBlockPosition().getX();
                int blockY = drillingWrapper.getBlockPosition().getY();
                int blockZ = drillingWrapper.getBlockPosition().getZ();
                User user = copy.getUser();

                World world = Bukkit.getWorld("world");
                Block block = world.getBlockAt(blockX, blockY, blockZ);

                if (user != null) {
                    Player player = Bukkit.getPlayer(user.getUUID());
                    if (player != null && player.getItemInHand().getType() == Material.HOPPER) {
                        player.setMetadata("drilling", new FixedMetadataValue(Astra.getPlugin(), true));
                        Bukkit.getScheduler().runTask(Astra.getPlugin(), () -> {
                            player.sendBlockChange(block.getLocation().add(0, 1, 0), Material.HOPPER.createBlockData());
                            startDrillAnimation(block, world, blockX, blockY, blockZ, user);
                        });
                    }
                }

                copy.cleanUp();
            });
        }
    }

    private void startDrillAnimation(Block block, World world, int x, int y, int z, User user) {
        new BukkitRunnable() {

            int currentY = y;
            int countblocks = 0;
            Location previousLocation = block.getLocation();

            @Override
            public void run() {
                //   if (currentY < 0) {
                //       removePreviousHopper();
                //       this.cancel();
                //       return;
                //   }
                //
                Player player = Bukkit.getPlayer(user.getUUID());
                //   if (player == null || !isPlayerMap(player, block)) {
                //       removePreviousHopper();
                //       this.cancel();
                //       return;
                //   }
                //
                //   if (currentY != y) { // Entferne den Hopper nur nach dem ersten Durchlauf
                //       removePreviousHopper();
                //   }

                Location location = new Location(world, x, currentY, z);
                player.sendBlockChange(location, Material.HOPPER.createBlockData());
                previousLocation = location;

                ArrayList<BlockState> blockStates = new ArrayList<>();
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            Block currentBlock = world.getBlockAt(x + dx, currentY + dy, z + dz);
                            if (isPlayerMap(player, currentBlock)) {
                                BlockState blockState = currentBlock.getState();
                                blockState.setType(Material.AIR);
                                blockStates.add(blockState);
                                countblocks++;
                            }
                        }
                    }
                }

                player.sendBlockChanges(blockStates);

                currentY--;

                if (currentY < 105) {
                    removePreviousHopper();
                    player.sendTitle(ChatColor.GREEN + "Blocks mined: ", ChatColor.WHITE + String.valueOf(countblocks), 10, 70, 20);
                    countblocks = 0;
                    this.cancel();
                }
            }

            private boolean isPlayerMap(Player player, Block block) {
                List<UUID> playerUUIDsForBlock = MineGenerator.getPlayerUUIDsForBlock(block, player.getUniqueId());
                return playerUUIDsForBlock.contains(player.getUniqueId());
            }

            private void removePreviousHopper() {
                if (previousLocation != null) {
                    Player player = Bukkit.getPlayer(user.getUUID());
                    if (player != null) {
                        player.sendBlockChange(previousLocation, Material.AIR.createBlockData());
                    }
                }
            }

        }.runTaskTimer(Astra.getPlugin(), 0L, 20L);
    }

}
