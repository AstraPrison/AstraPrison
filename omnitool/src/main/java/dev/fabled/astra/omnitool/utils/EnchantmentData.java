package dev.fabled.astra.omnitool.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class EnchantmentData {

    public static Map<String, EnchantmentData> enchantments = new HashMap<>(); // Stelle sicher, dass die Map initialisiert ist
    private final String name;
    private final boolean enabled;
    private final String type;
    private final String eventTrigger;
    private final double maxChance;
    private final int maxLevel;
    private final String currency;
    private final int startingCost;
    private final int increaseCostBy;
    private final int startingLevel;

    private final int omnitoollevel;
    private final String prestigeable;
    private final int maxprestige;
    private final List<String> actions;
    private final int slot;
    private final List<String> lore;
    private final String enchantitem;

    public EnchantmentData(String name, boolean enabled, String type, String eventTrigger, double maxChance,
                           int maxLevel, String currency, int startingCost, int increaseCostBy, int startingLevel, int omnitoollevel,
                           String prestigeable, int maxprestige, List<String> actions, int slot, String enchantitem, List<String> lore) {
        this.name = name;
        this.enabled = enabled;
        this.type = type;
        this.eventTrigger = eventTrigger;
        this.maxChance = maxChance;
        this.maxLevel = maxLevel;
        this.currency = currency;
        this.startingCost = startingCost;
        this.increaseCostBy = increaseCostBy;
        this.startingLevel = startingLevel;
        this.omnitoollevel = omnitoollevel;
        this.prestigeable = prestigeable;
        this.maxprestige = maxprestige;
        this.actions = actions;
        this.slot = slot;
        this.enchantitem = enchantitem;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getType() {
        return type;
    }

    public String getEventTrigger() {
        return eventTrigger;
    }

    public double getMaxChance() {
        return maxChance;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getCurrency() {
        return currency;
    }

    public int getStartingCost() {
        return startingCost;
    }

    public int getIncreaseCostBy() {
        return increaseCostBy;
    }

    public int getStartingLevel() {
        return startingLevel;
    }

    /**
     * Loads enchantment data from YAML files located in a enchantments folder.
     *
     * @param folderPath The path to the folder containing YAML files.
     * @return A map of enchantment names to EnchantmentData objects.
     */
    public static Map<String, EnchantmentData> loadEnchantmentsFromFiles(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            Bukkit.getLogger().warning("Enchantments folder not found or is not a directory");
            return enchantments;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            Bukkit.getLogger().warning("No files found in enchantments folder");
            return enchantments;
        }

        Yaml yaml = new Yaml();
        for (File file : files) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            String enchantName = config.getString("displayname");
            boolean enabled = config.getBoolean("enabled", true);
            String type = config.getString("type", "custom");
            String eventTrigger = config.getString("event-trigger", "BlockBreakEvent");
            double maxChance = config.getDouble("max-chance", 5.0);
            int maxLevel = config.getInt("max-level", 5000);
            String currency = config.getString("currency", "tokens");
            int startingCost = config.getInt("starting-cost", 100);
            int increaseCostBy = config.getInt("increase-cost-by", 400);
            int startingLevel = config.getInt("starting-level", 0);
            int omnitoollevel = config.getInt("omnitoollevel", 0);
            String prestige = config.getString("prestige", "false");
            int maxprestige = config.getInt("max-prestige", 0);
            List<String> actions = config.getStringList("actions");
            int slot = config.getInt("slot", 0);
            String enchantItem = config.getString("enchantitem");
            List<String> lore = config.getStringList("lore");

            Material material = Material.matchMaterial(enchantItem);
            if (material == null) {
                Bukkit.getLogger().log(Level.WARNING, "Invalid material name in file {0}: {1}", new Object[]{file.getName(), enchantItem});
                continue;
            }

            EnchantmentData enchantmentData = new EnchantmentData(
                    enchantName, enabled, type, eventTrigger, maxChance, maxLevel, currency,
                    startingCost, increaseCostBy, startingLevel, omnitoollevel, prestige, maxprestige, actions, slot, enchantItem, lore);
            enchantments.put(enchantName.toLowerCase(), enchantmentData);
            Bukkit.getLogger().info("Loaded enchantment: " + enchantName);
        }

        return enchantments;
    }

    public int getOmnitoolLevel() {
        return omnitoollevel;
    }

    public boolean isPrestigeable() {
        return "true".equalsIgnoreCase(prestigeable);
    }

    public List<String> getActions() {
        return actions;
    }

    public int getMaxPrestige() {
        return maxprestige;
    }

    public int getSlot() {
        return slot;
    }

    public String getEnchantitem() {
        return enchantitem;
    }

    public List<String> getLore() {
        return lore;
    }
}
