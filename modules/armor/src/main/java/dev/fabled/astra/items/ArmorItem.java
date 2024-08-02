package dev.fabled.astra.items;

import dev.fabled.astra.Astra;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArmorItem {

    private static final NamespacedKey RARITY_KEY = new NamespacedKey(Astra.getPlugin(), "rarity");
    private static final String[] RARITIES = {"Common", "Uncommon", "Rare", "Unique", "Legendary", "Masterful"};
    private static final Random RANDOM = new Random();

    public static ItemStack createArmorItem(String category, String type) {
        Material material = getMaterialFromType(type);
        ItemStack armorItem = new ItemStack(material);
        ItemMeta meta = armorItem.getItemMeta();

        if (meta instanceof LeatherArmorMeta leatherMeta) {
            leatherMeta.setColor(getArmorColor(category));
            leatherMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getCategoryDisplayName(category) + " " + getTypeDisplayName(category, type)));
            leatherMeta.setLore(createInitialLore());
            leatherMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            leatherMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            leatherMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            leatherMeta.getPersistentDataContainer().set(RARITY_KEY, PersistentDataType.STRING, getRandomRarity());
            armorItem.setItemMeta(leatherMeta);
        }

        return armorItem;
    }

    private static Material getMaterialFromType(String type) {
        switch (type.toLowerCase()) {
            case "helmet":
                return Material.LEATHER_HELMET;
            case "chestplate":
                return Material.LEATHER_CHESTPLATE;
            case "leggings":
                return Material.LEATHER_LEGGINGS;
            case "boots":
                return Material.LEATHER_BOOTS;
            default:
                throw new IllegalArgumentException("Unsupported armor type: " + type);
        }
    }

    private static Color getArmorColor(String category) {
        switch (category.toLowerCase()) {
            case "token":
                return Color.YELLOW;
            case "money":
                return Color.GREEN;
            case "prestige":
                return Color.RED;
            case "keys":
                return Color.ORANGE;
            default:
                return Color.WHITE;
        }
    }

    private static String getCategoryDisplayName(String category) {
        switch (category.toLowerCase()) {
            case "token":
                return ChatColor.YELLOW + ChatColor.BOLD.toString() + "Token";
            case "money":
                return ChatColor.GREEN + ChatColor.BOLD.toString() + "Money";
            case "prestige":
                return ChatColor.RED + ChatColor.BOLD.toString() + "Prestige";
            case "keys":
                return ChatColor.GOLD + ChatColor.BOLD.toString() + "Keys";
            default:
                return ChatColor.WHITE.toString();
        }
    }

    private static String getTypeDisplayName(String category, String type) {
        ChatColor color;
        switch (category.toLowerCase()) {
            case "token":
                color = ChatColor.YELLOW;
                break;
            case "money":
                color = ChatColor.GREEN;
                break;
            case "prestige":
                color = ChatColor.RED;
                break;
            case "keys":
                color = ChatColor.GOLD;
                break;
            default:
                color = ChatColor.WHITE;
                break;
        }

        return color + ChatColor.BOLD.toString() + type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
    }

    private static String getRandomRarity() {
        int index = RANDOM.nextInt(RARITIES.length);
        return RARITIES[index];
    }

    private static List<String> createInitialLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&fMine or drag n' drop similar equipment"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&fon top of each other to level up."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&lINFORMATION"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Rarity: &a" + getRandomRarity()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Level: &a0"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Boost: &a0"));
        return lore;
    }
}
