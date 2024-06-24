package dev.fabled.astra.items;

import dev.fabled.astra.Astra;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class DrillsItem {

    private static final String NAMESPACE_KEY = "drill_item_key";
    //private static final String ITEM_KEY = "drills_item";

    public static ItemStack createNormalDrill() {
        String ITEM_KEY = "normal_drill_item";
        ItemStack drillsItem = new ItemStack(Material.HOPPER);
        ItemMeta meta = drillsItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Normal Mine Drill");
            meta.setLore(Arrays.asList("", ChatColor.RED + "Drills a 3x3x3 hole", ChatColor.RED + "down to bedrock!", "", ChatColor.GRAY + "Right click to place!"));


            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(Astra.getPlugin(), NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            drillsItem.setItemMeta(meta);
        }
        return drillsItem;
    }

    public static ItemStack createBigDrill() {
        String ITEM_KEY = "big_drill_item";
        ItemStack drillsItem = new ItemStack(Material.HOPPER);
        ItemMeta meta = drillsItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Big Mine Drill");
            meta.setLore(Arrays.asList("", ChatColor.RED + "Drills a 5x5x5 hole", ChatColor.RED + "down to bedrock!", "", ChatColor.GRAY + "Right click to place!"));


            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(Astra.getPlugin(), NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            drillsItem.setItemMeta(meta);
        }
        return drillsItem;
    }

    public static ItemStack createUltraDrill() {
        String ITEM_KEY = "ultra_drill_item";
        ItemStack drillsItem = new ItemStack(Material.HOPPER);
        ItemMeta meta = drillsItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Ultra Mine Drill");
            meta.setLore(Arrays.asList("", ChatColor.RED + "Drills a 9x9x9 hole", ChatColor.RED + "down to bedrock!", "", ChatColor.GRAY + "Right click to place!"));


            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(Astra.getPlugin(), NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            drillsItem.setItemMeta(meta);
        }
        return drillsItem;
    }


    public static boolean isDrillsItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        String itemKey = data.get(new NamespacedKey(Astra.getPlugin(), "drill_item_key"), PersistentDataType.STRING);
        return itemKey != null && (itemKey.equals("normal_drill_item") || itemKey.equals("big_drill_item") || itemKey.equals("ultra_drill_item"));
    }

    public static String getDrillItemKey(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.get(new NamespacedKey(Astra.getPlugin(), "drill_item_key"), PersistentDataType.STRING);
    }

    public static boolean getdrillkey(ItemStack item, String itemKey) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        return itemKey.equals(data.get(new NamespacedKey(Astra.getPlugin(), NAMESPACE_KEY), PersistentDataType.STRING));
    }

}
