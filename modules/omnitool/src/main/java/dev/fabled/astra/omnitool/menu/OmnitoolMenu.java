package dev.fabled.astra.omnitool.menu;

import dev.fabled.astra.Astra;
import dev.fabled.astra.omnitool.utils.EnchantmentData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class OmnitoolMenu implements InventoryHolder, Listener {

    private Inventory inventory;

    public OmnitoolMenu() {
    }

    private void initializeItems(Player player) {
        inventory = Bukkit.createInventory(this, 54, "Omnitool Inventory");
        for (String key : EnchantmentData.enchantments.keySet()) {
            EnchantmentData enchantmentData = EnchantmentData.enchantments.get(key);
            Material material = Material.matchMaterial(enchantmentData.getEnchantitem());
            if (material == null) {
                Bukkit.getLogger().log(Level.WARNING, "Invalid material name for enchantment {0}: {1}", new Object[]{key, enchantmentData.getEnchantitem()});
                continue;
            }

            int slot = enchantmentData.getSlot();
            if (slot < 0 || slot >= inventory.getSize()) {
                Bukkit.getLogger().log(Level.WARNING, "Invalid slot number for enchantment {0}: {1}", new Object[]{key, slot});
                continue;
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(enchantmentData.getName());

            List<String> loreLines = enchantmentData.getLore();
            if (loreLines != null && !loreLines.isEmpty()) {
                List<String> coloredLore = new ArrayList<>();
                for (String line : loreLines) {
                    String replacedLine = replacePlaceholders(ChatColor.translateAlternateColorCodes('&', line), enchantmentData, player);
                    coloredLore.add(replacedLine);
                }
                meta.setLore(coloredLore);
            }

            item.setItemMeta(meta);
            inventory.setItem(slot, item);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Inventory getInventory(Player player) {
        initializeItems(player);  // Items werden jedes Mal neu initialisiert
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null || !event.getView().getTitle().equals("Omnitool Inventory")) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        Player player = (Player) event.getWhoClicked();
        String enchantName = null;

        for (Map.Entry<String, EnchantmentData> entry : EnchantmentData.enchantments.entrySet()) {
            Material material = Material.matchMaterial(entry.getValue().getEnchantitem());
            if (material != null && material == clickedItem.getType()) {
                enchantName = entry.getKey();
                break;
            }
        }

        if (enchantName != null) {
            EnchantmentData enchantmentData = EnchantmentData.enchantments.get(enchantName);
            int requiredLevel = enchantmentData.getOmnitoolLevel();

            int playerLevel = getEnchantLevel(player, enchantmentData);
            if (playerLevel < requiredLevel) {
                player.sendMessage(ChatColor.YELLOW + "Your omnitool have to be level " + ChatColor.RED + requiredLevel + ChatColor.YELLOW + " to use this enchantment.");
                event.setCancelled(true);
                return;
            }

            EnchantGui enchantGui = new EnchantGui(player, enchantName);
            player.openInventory(enchantGui.getInventory());
        }
        event.setCancelled(true);
    }

    private String replacePlaceholders(String line, EnchantmentData enchantmentData, Player player) {
        line = line.replace("{currentlevel}", String.valueOf(getEnchantLevel(player, enchantmentData)));
        line = line.replace("{maxlevel}", String.valueOf(enchantmentData.getMaxLevel()));
        line = line.replace("{activationchance}", String.valueOf(enchantmentData.getMaxChance()));
        line = line.replace("{upgradecost1}", getUpgradeCost(getEnchantLevel(player, enchantmentData), 1, enchantmentData.getName().toLowerCase()));
        line = line.replace("{upgradecost10}", getUpgradeCost(getEnchantLevel(player, enchantmentData), 10, enchantmentData.getName().toLowerCase()));
        line = line.replace("{upgradecost100}", getUpgradeCost(getEnchantLevel(player, enchantmentData), 100, enchantmentData.getName().toLowerCase()));

        int requiredLevel = enchantmentData.getOmnitoolLevel();
        line = line.replace("{omnitoollevel}", "" + requiredLevel);

        return line;
    }

    private String getUpgradeCost(int currentLevel, int levels, String key) {
        EnchantmentData enchantmentData = EnchantmentData.enchantments.get(key);
        int cost = 0;
        for (int i = 0; i < levels; i++) {
            cost += enchantmentData.getStartingCost() + (currentLevel + i) * enchantmentData.getIncreaseCostBy();
        }
        return String.valueOf(cost);
    }

    private int getEnchantLevel(Player player, EnchantmentData enchantmentData) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() != Material.DIAMOND_PICKAXE) {
            return 0;
        }
        ItemMeta itemMeta = itemInHand.getItemMeta();
        if (itemMeta == null) {
            return 0;
        }
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        NamespacedKey enchantKey = new NamespacedKey(Astra.getPlugin(), enchantmentData.getName().toLowerCase());
        return pdc.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0);
    }
}
