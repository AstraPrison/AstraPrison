package dev.fabled.astra.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import dev.fabled.astra.Astra;
import dev.fabled.astra.items.BombItem;
import dev.fabled.astra.mines.generator.MineGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BombsPacketHandler extends PacketListenerAbstract implements Listener {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Map<UUID, Integer> activeBombs = new HashMap<>();

    public BombsPacketHandler() {
        super(PacketListenerPriority.NORMAL);
    }

    public static int getActiveBombs(UUID playerUUID) {
        return activeBombs.getOrDefault(playerUUID, 0);
    }

    public static void startBombAnimation(Block block, World world, int x, int y, int z, Player player, String mineName, ItemStack bombItem) {
        if (player == null || world == null) return;

        JavaPlugin plugin = Astra.getPlugin();
        int radius;

        if (BombItem.isBombItem(bombItem, plugin, "normal_bomb_item")) {
            radius = 3;
        } else if (BombItem.isBombItem(bombItem, plugin, "big_bomb_item")) {
            radius = 5;
        } else if (BombItem.isBombItem(bombItem, plugin, "ultra_bomb_item")) {
            radius = 9;
        } else {
            return;
        }

        List<BlockState> originalBlockStates = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockState state = block.getRelative(dx, dy, dz).getState();
                    originalBlockStates.add(state);
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player == null || world == null) return;

            //world.createExplosion(x, y, z, 4, false, false);
            player.spawnParticle(Particle.EXPLOSION, block.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.1);

            List<BlockState> blockStatesToRemove = getBlocksInSphere(player, world, x, y, z, radius);
            player.sendBlockChanges(blockStatesToRemove);
            for (BlockState blockState : blockStatesToRemove) {
                blockState.removeMetadata("material", plugin);
            }

        }, 1L);
    }


    //   @Override
    // public void onPacketReceive(PacketReceiveEvent event) {
    //     if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
    //         PacketReceiveEvent copy = event.clone();
    //         EXECUTOR.execute(() -> {
    //             WrapperPlayClientPlayerBlockPlacement bombWrapper = new WrapperPlayClientPlayerBlockPlacement(copy);
    //             int blockX = bombWrapper.getBlockPosition().getX();
    //             int blockY = bombWrapper.getBlockPosition().getY();
    //             int blockZ = bombWrapper.getBlockPosition().getZ();
    //             User user = copy.getUser();
    //
    //             World world = Bukkit.getWorld("world");
    //             Block block = world.getBlockAt(blockX, blockY, blockZ);
    //             if (user != null) {
    //                 Player player = Bukkit.getPlayer(user.getUUID());
    //                 if (player != null && BombItem.isBombItem(player.getItemInHand(), Astra.getPlugin())) {
    //                     player.setMetadata("bomb", new FixedMetadataValue(Astra.getPlugin(), true));
    //                     Bukkit.getScheduler().runTask(Astra.getPlugin(), () -> {
    //                         String mineName = block.getLocation().getBlock().getMetadata("mineName").get(0).asString();
    //                         block.setType(Material.AIR);
    //                         //player.sendBlockChange(block.getLocation().add(0, 1, 0), Material.TNT.createBlockData());
    //
    //                         startBombAnimation(block, world, blockX, blockY, blockZ, user, mineName, player.getItemInHand());
    //                     });
    //                 }
    //             }
    //
    //             copy.cleanUp();
    //         });
    //     }
    // }

    private static List<BlockState> getBlocksInSphere(Player player, World world, int centerX, int centerY, int centerZ, int radius) {
        List<BlockState> blockStates = new ArrayList<>();
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                    if (Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) + (z - centerZ) * (z - centerZ)) <= radius) {
                        Block block = world.getBlockAt(x, y, z);
                        if (isPlayerMap(player, block)) {
                            BlockState blockState = block.getState();
                            blockState.setType(Material.AIR);
                            blockStates.add(blockState);
                        }
                    }
                }
            }
        }
        return blockStates;
    }

    public static List<BlockState> getAffectedBlocks(ItemStack item, Player player, World world, int centerX, int centerY, int centerZ) {
        JavaPlugin plugin = (JavaPlugin) player.getServer().getPluginManager().getPlugin("YourPluginName");
        if (plugin == null) return new ArrayList<>();

        if (BombItem.isBombItem(item, plugin, "normal_bomb_item")) {
            return getBlocksInSphere(player, world, centerX, centerY, centerZ, 3);
        } else if (BombItem.isBombItem(item, plugin, "big_bomb_item")) {
            return getBlocksInSphere(player, world, centerX, centerY, centerZ, 5);
        } else if (BombItem.isBombItem(item, plugin, "ultra_bomb_item")) {
            return getBlocksInSphere(player, world, centerX, centerY, centerZ, 9);
        } else {
            return new ArrayList<>();
        }
    }

    private static boolean isPlayerMap(Player player, Block block) {
        List<UUID> playerUUIDsForBlock = MineGenerator.getPlayerUUIDsForBlock(block, player.getUniqueId());
        return playerUUIDsForBlock.contains(player.getUniqueId());
    }

    @EventHandler
    public void bombdrop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        JavaPlugin plugin = Astra.getPlugin();

        if (BombItem.isBombItem(itemInHand, plugin, "normal_bomb_item") ||
                BombItem.isBombItem(itemInHand, plugin, "big_bomb_item") ||
                BombItem.isBombItem(itemInHand, plugin, "ultra_bomb_item")) {

            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                //player.sendMessage("Drop Bomb");

                World world = player.getWorld();
                Location startLocation = player.getLocation();

                TNTPrimed tnt = world.spawn(startLocation, TNTPrimed.class);
                tnt.setVelocity(startLocation.getDirection().normalize().multiply(0));

                AtomicBoolean hasHitBlock = new AtomicBoolean(false);
                Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    if (!hasHitBlock.get()) {
                        Location location = tnt.getLocation();
                        int radius = 1;

                        for (int x = -radius; x <= radius; ++x) {
                            for (int y = -radius; y <= radius; ++y) {
                                for (int z = -radius; z <= radius; ++z) {
                                    Location blockLocation = location.clone().add(x, y, z);
                                    Block block = blockLocation.getBlock();

                                    if (block.hasMetadata("material")) {
                                        startBombAnimation(
                                                block,
                                                world,
                                                blockLocation.getBlockX(),
                                                blockLocation.getBlockY(),
                                                blockLocation.getBlockZ(),
                                                player,
                                                "mine",
                                                itemInHand
                                        );

                                        tnt.remove();
                                        hasHitBlock.set(true);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }, 0L, 1L);

                event.setCancelled(true);
            }
        }
    }
}
