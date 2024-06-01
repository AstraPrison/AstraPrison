package dev.fabled.astra.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class DrillsItem {

    private static final String NAMESPACE_KEY = "astra";
    private static final String ITEM_KEY = "drills_item";

    public static ItemStack createNormalDrill(JavaPlugin plugin) {
        ItemStack drillsItem = new ItemStack(Material.HOPPER);
        ItemMeta meta = drillsItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Normal Mine Drill");
            meta.setLore(Arrays.asList("", ChatColor.RED + "Drills a 3x3x3 hole", ChatColor.RED + "down to bedrock!", "", ChatColor.GRAY + "Right click to place!"));


            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            drillsItem.setItemMeta(meta);
        }
        return drillsItem;
    }

    public static ItemStack createBigDrill(JavaPlugin plugin) {
        ItemStack drillsItem = new ItemStack(Material.HOPPER);
        ItemMeta meta = drillsItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Big Mine Drill");
            meta.setLore(Arrays.asList("", ChatColor.RED + "Drills a 5x5x5 hole", ChatColor.RED + "down to bedrock!", "", ChatColor.GRAY + "Right click to place!"));


            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            drillsItem.setItemMeta(meta);
        }
        return drillsItem;
    }

    public static ItemStack createUltraDrill(JavaPlugin plugin) {
        ItemStack drillsItem = new ItemStack(Material.HOPPER);
        ItemMeta meta = drillsItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Ultra Mine Drill");
            meta.setLore(Arrays.asList("", ChatColor.RED + "Drills a 9x9x9 hole", ChatColor.RED + "down to bedrock!", "", ChatColor.GRAY + "Right click to place!"));


            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            drillsItem.setItemMeta(meta);
        }
        return drillsItem;
    }


    public static boolean isDrillsItem(ItemStack item, JavaPlugin plugin) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        return ITEM_KEY.equals(data.get(new NamespacedKey(plugin, NAMESPACE_KEY), PersistentDataType.STRING));
    }

}
