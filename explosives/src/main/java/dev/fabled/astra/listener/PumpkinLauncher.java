package dev.fabled.astra.listener;


import dev.fabled.astra.mines.generator.MineGenerator;
import dev.fabled.astra.utils.ExplosiveReset;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PumpkinLauncher implements Listener {
    public static List<Block> destroyedBlocks = new ArrayList();
    private final List<Location> destroyedBlockLocations;
    Map<Player, Integer> playerUses = new HashMap<>();
    private final Plugin plugin;
    private final FileConfiguration rpgConfig;
    private final Map<String, Boolean> rarities;
    private final Set<Player> usedPlayers = new HashSet();
    private final int destroyedBlocksCount = 0;

    public PumpkinLauncher(Plugin plugin, Map<String, Boolean> rarities, FileConfiguration rpgConfig) {
        this.plugin = plugin;
        this.rpgConfig = rpgConfig;
        this.destroyedBlockLocations = new ArrayList();
        this.rarities = rarities;
    }

    private static boolean areBlockStatesEqual(BlockState blockState1, UUID playerUUID) {
        boolean hasPlayerUUIDMetadata = blockState1.hasMetadata("playerUUID");
        if (hasPlayerUUIDMetadata) {
            List<MetadataValue> metadataValues = blockState1.getMetadata("playerUUID");
            Iterator var4 = metadataValues.iterator();

            while (true) {
                MetadataValue value;
                do {
                    if (!var4.hasNext()) {
                        return false;
                    }

                    value = (MetadataValue) var4.next();
                } while (!(value.value() instanceof Map));

                Map<BlockState, List<UUID>> map = (Map) value.value();
                Iterator var7 = map.entrySet().iterator();

                while (var7.hasNext()) {
                    Map.Entry<BlockState, List<UUID>> entry = (Map.Entry) var7.next();
                    if (entry.getValue().contains(playerUUID)) {
                        return true;
                    }
                }
            }
        } else {
            return false;
        }
    }

    public static String extractRarity(String displayName, Map<String, Boolean> rarities) {
        String strippedName = ChatColor.stripColor(displayName);
        for (String rarity : rarities.keySet()) {
            if (strippedName.toLowerCase().contains(rarity.toLowerCase())) {
                return rarity;
            } else {
                //System.out.println("Rarity not found: " + rarity);
            }
        }
        return null;
    }

    @EventHandler
    public void onPumpkinLauncherUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item != null && item.getType().equals(Material.DIAMOND_HOE) && item.hasItemMeta()) {
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    String rarity = extractRarity(meta.getDisplayName(), rarities);
                    if (rarity != null) {
                        int uses = playerUses.getOrDefault(player, rpgConfig.getInt("thresholds." + rarity + ".uses"));
                        if (uses > 0) {
                            Snowball projectile = player.launchProjectile(Snowball.class);
                            String materialName = rpgConfig.getString("ammunition.material");
                            Material material = Material.matchMaterial(materialName);
                            ItemStack pumpkinItem = new ItemStack(material, 1);
                            ItemMeta pumpkinMeta = pumpkinItem.getItemMeta();
                            pumpkinMeta.setDisplayName("Flying Pumpkin");
                            pumpkinItem.setItemMeta(pumpkinMeta);
                            projectile.setItem(pumpkinItem);
                            projectile.setGravity(false);
                            projectile.setVelocity(projectile.getVelocity().multiply(rpgConfig.getDouble("ammunition.velocity-multiplier")));
                            double slowdownFactor = 0.5D;
                            projectile.setVelocity(projectile.getVelocity().multiply(slowdownFactor));
                            double scale = 35.0D;
                            this.scheduleRemoval(projectile);

                            uses--;
                            playerUses.put(player, uses);

                            List<String> updatedLore = new ArrayList<>();
                            List<String> configLore = rpgConfig.getStringList("lore");
                            for (String line : configLore) {
                                line = line.replace("{uses}", String.valueOf(uses));
                                updatedLore.add(ChatColor.translateAlternateColorCodes('&', line));
                            }

                            meta.setLore(updatedLore);
                            item.setItemMeta(meta);
                            player.getInventory().setItemInMainHand(item);
                        } else {
                            player.getInventory().removeItem(item);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', rpgConfig.getString("outofammo")));
                            playerUses.remove(player);
                            this.usedPlayers.remove(player);
                        }


                    }
                }
            }

        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof Snowball && projectile.getShooter() instanceof Player player) {
            AtomicBoolean hasHitBlock = new AtomicBoolean(false);
            Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
                if (!hasHitBlock.get()) {
                    Location location = projectile.getLocation();
                    int radius = 1;

                    for (int x = -radius; x <= radius; ++x) {
                        for (int y = -radius; y <= radius; ++y) {
                            for (int z = -radius; z <= radius; ++z) {
                                Location blockLocation = location.clone().add(x, y, z);
                                Block block = blockLocation.getBlock();
                                Material blockType = block.getType();
                                if (block.hasMetadata(rpgConfig.getString("mine-metadata"))) {
                                    String displayName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                                    String rarity = extractRarity(displayName, rarities);
                                    this.sendSphericalExplosion(player, blockLocation, rpgConfig.getInt("thresholds." + rarity + ".currencies.radius"));
                                    hasHitBlock.set(true);
                                    projectile.remove();
                                    this.usedPlayers.remove(player);
                                    MineGenerator.removeBlockFromMap(block.getState(), player.getUniqueId());
                                    return;
                                }
                            }
                        }
                    }
                }

            }, 0L, 1L);
            hasHitBlock.set(false);
        }

    }

    private void sendSphericalExplosion(Player player, Location blockLocation, int radius) {
        int radiusSquared = radius * radius;
        List<BlockState> blockStates = new ArrayList<>();

        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    if (x * x + y * y + z * z <= radiusSquared) {
                        Location currentLocation = blockLocation.clone().add(x, y, z);
                        Block currentBlock = currentLocation.getBlock();
                        if (areBlockStatesEqual(currentBlock.getState(), player.getUniqueId())) {
                            BlockState blockState = currentBlock.getState();
                            blockState.setType(Material.AIR);
                            blockState.update(true, false);
                            blockState.removeMetadata(rpgConfig.getString("mine-metadata"), this.plugin);
                            blockStates.add(blockState);

                            if (rpgConfig.getBoolean("use-explosion-particles")) {
                                currentBlock.getWorld().spawnParticle(Particle.EXPLOSION, currentLocation.add(0.5D, 0.5D, 0.5D), 1);
                            }

                            int destroyedBlocksCount = this.destroyedBlockLocations.size();
                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                            String displayName = heldItem.getItemMeta().getDisplayName();
                            String rarity = extractRarity(displayName, rarities);
                            String mineName = currentBlock.getMetadata("mineName").get(0).asString();
                            UUID userUUID = player.getUniqueId();
                            ExplosiveReset.updateBlockCount(userUUID, mineName);
                            if (ExplosiveReset.shouldResetMine(userUUID, mineName)) {
                                ExplosiveReset.resetMine(player, mineName);
                            }
                        }
                    }
                }
            }
        }

        BlockState[] blockStatesArray = blockStates.toArray(new BlockState[0]);
        player.sendBlockChanges(Arrays.asList(blockStatesArray));

        List<Location> affectedLocations = new ArrayList<>();
        for (BlockState state : blockStates) {
            affectedLocations.add(state.getLocation());
        }

        this.destroyedBlockLocations.addAll(affectedLocations);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (rpgConfig.getBoolean("use-packets") && message.startsWith("/" + rpgConfig.getString("mine-reset-command"))) {
            this.destroyedBlockLocations.clear();
        }

    }

    private boolean isBlockLocationInList(Location location) {
        Iterator var2 = this.destroyedBlockLocations.iterator();

        Location loc;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            loc = (Location) var2.next();
        } while (loc.getBlockX() != location.getBlockX() || loc.getBlockY() != location.getBlockY() || loc.getBlockZ() != location.getBlockZ());

        return true;
    }

    private void scheduleRemoval(Snowball projectile) {
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
            if (!projectile.isDead()) {
                projectile.remove();
            }

        }, 50L);
    }
}