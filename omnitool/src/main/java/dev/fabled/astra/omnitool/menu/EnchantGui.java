package dev.fabled.astra.omnitool.menu;

import dev.fabled.astra.Astra;
import dev.fabled.astra.omnitool.utils.EnchantmentData;
import dev.fabled.astra.utils.MiniColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantGui implements InventoryHolder {

    private final Inventory inv;

    private final NamespacedKey enchantKey;
    private final Player player;
    private final String enchant;
    private final EnchantmentData enchantmentData;

    public EnchantGui(Player player, String enchant) {
        this.player = player;
        this.enchant = enchant.toLowerCase();
        this.enchantKey = new NamespacedKey(Astra.getPlugin(), this.enchant);
        this.enchantmentData = EnchantmentData.enchantments.get(enchant);
        inv = Bukkit.createInventory(this, 36, ChatColor.translateAlternateColorCodes('&', "&7Upgrading " + enchant));
        initializeItems();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public void initializeItems() {
        inv.setItem(11, createGuiItem(Material.LIME_STAINED_GLASS_PANE, "<red><bold>+1 level", Arrays.asList(
                MiniColor.INVENTORY.deserialize(""),
                MiniColor.INVENTORY.deserialize("<white>Current Level: " + getEnchantLevel()),
                MiniColor.INVENTORY.deserialize("<white>Max Level: " + enchantmentData.getMaxLevel()),
                MiniColor.INVENTORY.deserialize("<green>Cost: " + getUpgradeCost(1))
        )));
        inv.setItem(12, createGuiItem(Material.LIME_STAINED_GLASS_PANE, "<red><bold>+10 levels", Arrays.asList(
                MiniColor.INVENTORY.deserialize(""),
                MiniColor.INVENTORY.deserialize("<white>Current Level: " + getEnchantLevel()),
                MiniColor.INVENTORY.deserialize("<white>Max Level: " + enchantmentData.getMaxLevel()),
                MiniColor.INVENTORY.deserialize("<green>Cost: " + getUpgradeCost(10))
        )));
        inv.setItem(13, createGuiItem(Material.LIME_STAINED_GLASS_PANE, "<red><bold>+100 levels", Arrays.asList(
                MiniColor.INVENTORY.deserialize(""),
                MiniColor.INVENTORY.deserialize("<white>Current Level: " + getEnchantLevel()),
                MiniColor.INVENTORY.deserialize("<white>Max Level: " + enchantmentData.getMaxLevel()),
                MiniColor.INVENTORY.deserialize("<green>Cost: " + getUpgradeCost(100))
        )));
        inv.setItem(14, createGuiItem(Material.LIME_STAINED_GLASS_PANE, "<red><bold>+1k levels", Arrays.asList(
                MiniColor.INVENTORY.deserialize(""),
                MiniColor.INVENTORY.deserialize("<white>Current Level: " + getEnchantLevel()),
                MiniColor.INVENTORY.deserialize("<white>Max Level: " + enchantmentData.getMaxLevel()),
                MiniColor.INVENTORY.deserialize("<green>Cost: " + getUpgradeCost(1000))
        )));
        inv.setItem(15, createGuiItem(Material.LIME_STAINED_GLASS_PANE, "<red><bold>Max Upgrade", Arrays.asList(
                MiniColor.INVENTORY.deserialize(""),
                MiniColor.INVENTORY.deserialize("<white>Current Level: " + getEnchantLevel()),
                MiniColor.INVENTORY.deserialize("<white>Max Level: " + enchantmentData.getMaxLevel()),
                MiniColor.INVENTORY.deserialize("<green>Cost: " + getMaxUpgradeCost())
        )));
        inv.setItem(29, createGuiItem(Material.TRIPWIRE_HOOK, "<red><bold>Toggle enchant", Arrays.asList(
                MiniColor.INVENTORY.deserialize(""),
                MiniColor.INVENTORY.deserialize("<white>Current status: " + (isEnchantDisabled() ? "<red>Disabled" : "<green>Enabled"))
        )));
        inv.setItem(31, createGuiItem(Material.ANVIL, "<red><bold>Refund levels", Arrays.asList(
                MiniColor.INVENTORY.deserialize(""),
                MiniColor.INVENTORY.deserialize("<white>Returns: " + getRefundAmount())
        )));
        inv.setItem(33, createGuiItem(Material.NETHER_STAR, "<red><bold>Prestige enchant", Arrays.asList(
                MiniColor.INVENTORY.deserialize(""),
                MiniColor.INVENTORY.deserialize("<white>Your enchant needs to be at Max-Level to enchant it"),
                MiniColor.INVENTORY.deserialize("<white>Your enchant is at Max-Level, enchant it!")
        )));
        List<Integer> redSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 34, 35);
        for (int slot : redSlots) {
            inv.setItem(slot, createGuiItem(Material.RED_STAINED_GLASS_PANE, "", new ArrayList<>()));
        }
    }

    protected ItemStack createGuiItem(final Material material, final String name, final List<Component> lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(MiniColor.INVENTORY.deserialize(name));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private int getEnchantLevel() {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() != Material.DIAMOND_PICKAXE) {
            return 0;
        }
        ItemMeta itemMeta = itemInHand.getItemMeta();
        if (itemMeta == null) {
            return 0;
        }
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        return pdc.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0);
    }

    public String getEnchant() {
        return enchant;
    }

    private String getUpgradeCost(int levels) {
        int costPerLevel = enchantmentData.getStartingCost() + (levels - 1) * enchantmentData.getIncreaseCostBy();
        return String.valueOf(levels * costPerLevel);
    }

    private String getMaxUpgradeCost() {
        int currentLevel = getEnchantLevel();
        int remainingLevels = enchantmentData.getMaxLevel() - currentLevel;
        int totalCost = 0;
        for (int i = 0; i < remainingLevels; i++) {
            totalCost += enchantmentData.getStartingCost() + i * enchantmentData.getIncreaseCostBy();
        }
        return String.valueOf(totalCost);
    }

    private String getRefundAmount() {
        return "0";
    }

    private boolean disableEnchant() {
        return true;
    }

    private boolean isEnchantDisabled() {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() != Material.DIAMOND_PICKAXE) {
            return false;
        }
        ItemMeta itemMeta = itemInHand.getItemMeta();
        if (itemMeta == null) {
            return false;
        }
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        return pdc.has(new NamespacedKey(Astra.getPlugin(), "disabled_" + enchant), PersistentDataType.INTEGER);
    }
}
