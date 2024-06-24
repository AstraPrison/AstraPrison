package dev.fabled.astra.listener;

import dev.fabled.astra.Astra;
import dev.fabled.astra.mines.generator.MineGenerator;
import dev.fabled.astra.omnitool.actions.changelayer;
import dev.fabled.astra.omnitool.actions.explode;
import dev.fabled.astra.omnitool.actions.removelayer;
import dev.fabled.astra.omnitool.actions.spawn;
import dev.fabled.astra.omnitool.utils.EnchantmentData;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EnchantsTriggerEventListener implements Listener {

    private static EnchantsTriggerEventListener instance;
    private final Random random;

    public EnchantsTriggerEventListener() {
        random = new Random();
    }

    public static EnchantsTriggerEventListener getInstance() {
        if (instance == null) {
            instance = new EnchantsTriggerEventListener();
        }
        return instance;
    }

    public static boolean isPlayerMap(Player player, Block block) {
        List<UUID> playerUUIDsForBlock = MineGenerator.getPlayerUUIDsForBlock(block, player.getUniqueId());
        return playerUUIDsForBlock.contains(player.getUniqueId());
    }

    public boolean hasEnchantment(ItemStack item, NamespacedKey enchantKey) {
        if (item == null || item.getType() != Material.DIAMOND_PICKAXE) return false;

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return false;

        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        return pdc.has(enchantKey, PersistentDataType.INTEGER);
    }

    public boolean shouldTriggerEnchantment(double chance) {
        return random.nextDouble() < (chance / 100.0);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (block.hasMetadata("mineName")) {
            String mineName = block.getMetadata("mineName").get(0).asString();

            for (EnchantmentData enchantment : EnchantmentData.enchantments.values()) {
                if (enchantment.isEnabled() && "BlockBreakEvent".equals(enchantment.getEventTrigger())) {
                    NamespacedKey enchantKey = new NamespacedKey(Astra.getPlugin(), enchantment.getName());

                    if (hasEnchantment(itemInHand, enchantKey)) {
                        if (shouldTriggerEnchantment(enchantment.getMaxChance())) {
                            triggerEnchantment(player, enchantment.getName(), block, mineName);
                        }
                    }
                }
            }
        }
    }

    public void triggerEnchantment(Player player, String enchantName, Block block, String mineName) {
        EnchantmentData enchantmentData = EnchantmentData.enchantments.get(enchantName.toLowerCase());
        if (enchantmentData != null) {
            for (String action : enchantmentData.getActions()) {
                String[] parts = action.split(" ");
                String actionType = parts[0];

                switch (actionType) {
                    case "removelayer":
                        new removelayer(player, block);
                        break;
                    case "changelayer":
                        new changelayer(player, block, action);
                        break;
                    case "explode":
                        new explode(player, block, action);
                        break;
                    case "spawn":
                        new spawn(player, block, action);
                        break;
                    default:
                        player.sendMessage(MiniColor.parse("<red>Unknown action: " + action));
                        break;
                }
            }
            player.sendMessage(MiniColor.parse("<red>" + enchantName + "</red>" + "<white> has triggered!"));
        } else {
            player.sendMessage(MiniColor.parse("<red>Enchantment data is null for enchantment: " + enchantName));
        }
    }

    public EnchantmentData getEnchantmentData(String enchantName) {
        return EnchantmentData.enchantments.get(enchantName);
    }
}
