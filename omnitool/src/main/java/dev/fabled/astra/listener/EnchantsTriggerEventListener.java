package dev.fabled.astra.listener;

import dev.fabled.astra.Astra;
import dev.fabled.astra.omnitool.utils.EnchantmentData;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnchantsTriggerEventListener implements Listener {

    private final Map<String, EnchantmentData> enchantments;
    private final Random random;

    public EnchantsTriggerEventListener() {
        enchantments = new HashMap<>();
        random = new Random();
    }

    public void loadEnchantments() {
        File enchantmentsFolder = new File(Astra.getPlugin().getDataFolder(), "enchantments");
        if (!enchantmentsFolder.exists()) {
            enchantmentsFolder.mkdirs();
        }

        File[] files = enchantmentsFolder.listFiles();
        if (files != null) {
            StringBuilder loadedEnchantments = new StringBuilder("Loaded enchantments: ");
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".yml")) {
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    String enchantName = file.getName().replace(".yml", "");
                    boolean enabled = config.getBoolean("enabled", true);
                    String type = config.getString("type", "custom");
                    String eventTrigger = config.getString("event-trigger", "BlockBreakEvent");
                    double maxChance = config.getDouble("max-chance", 5.0);
                    int maxLevel = config.getInt("max-level", 5000);
                    String currency = config.getString("currency", "tokens");
                    int startingCost = config.getInt("starting-cost", 100);
                    int increaseCostBy = config.getInt("increase-cost-by", 400);
                    int startingLevel = config.getInt("starting-level", 0);
                    List<String> actions = config.getStringList("actions");

                    EnchantmentData enchantmentData = new EnchantmentData(
                            enchantName, enabled, type, eventTrigger, maxChance, maxLevel, currency,
                            startingCost, increaseCostBy, startingLevel, actions);
                    enchantments.put(enchantName, enchantmentData);

                    loadedEnchantments.append(enchantName).append(", ");
                }
            }
            if (loadedEnchantments.length() > 20) {
                loadedEnchantments.setLength(loadedEnchantments.length() - 2);
            }
            AstraLog.log(AstraLogLevel.SUCCESS, loadedEnchantments.toString());
        }
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        String mineName = "exampleMine";

        for (EnchantmentData enchantment : enchantments.values()) {
            if (enchantment.isEnabled() && "BlockBreakEvent".equals(enchantment.getEventTrigger())) {
                NamespacedKey enchantKey = new NamespacedKey(Astra.getPlugin(), enchantment.getName());

                if (hasEnchantment(itemInHand, enchantKey) && shouldTriggerEnchantment(enchantment.getMaxChance())) {
                    triggerEnchantment(player, enchantment.getName(), block, mineName);
                }
            }
        }
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

    public void triggerEnchantment(Player player, String enchantName, Block block, String mineName) {
        EnchantmentData enchantmentData = enchantments.get(enchantName);
        if (enchantmentData != null) {
            for (String action : enchantmentData.getActions()) {
                if (action.startsWith("command")) {
                    String command = action.replace("command ", "").replace("%player%", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
            player.sendMessage(ChatColor.RED + enchantName + ChatColor.WHITE + " has triggered!");
        }
    }
}
