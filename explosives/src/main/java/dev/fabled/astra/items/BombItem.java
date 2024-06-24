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

public class BombItem {

    private static final String NAMESPACE_KEY = "astra";

    public static ItemStack createNormalBomb() {
        String ITEM_KEY = "normal_bomb_item";
        ItemStack bombItem = new ItemStack(Material.TNT);
        ItemMeta meta = bombItem.getItemMeta();
        if (meta != null) {
            String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Common Grenade";
            meta.setDisplayName(displayName);
            String loreDisplayName = displayName.replace(" Grenade", "");
            meta.setLore(Arrays.asList("", ChatColor.WHITE + "Throw this " + ChatColor.GREEN + loreDisplayName, ChatColor.WHITE + "grenade onto your mine", ChatColor.WHITE + "to form a huge explosion!", "", ChatColor.GRAY + "(( Right click to use ))"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(Astra.getPlugin(), NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            bombItem.setItemMeta(meta);
        }
        return bombItem;
    }

    public static ItemStack createBigBomb() {
        String ITEM_KEY = "big_bomb_item";
        ItemStack bombItem = new ItemStack(Material.TNT);
        ItemMeta meta = bombItem.getItemMeta();
        if (meta != null) {
            String displayName = ChatColor.AQUA + "" + ChatColor.BOLD + "Rare Grenade";
            meta.setDisplayName(displayName);
            String loreDisplayName = displayName.replace(" Grenade", "");
            meta.setLore(Arrays.asList("", ChatColor.WHITE + "Throw this " + ChatColor.AQUA + loreDisplayName, ChatColor.WHITE + "grenade onto your mine", ChatColor.WHITE + "to form a huge explosion!", "", ChatColor.GRAY + "(( Right click to use ))"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(Astra.getPlugin(), NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            bombItem.setItemMeta(meta);
        }
        return bombItem;
    }

    public static ItemStack createUltraBomb() {
        String ITEM_KEY = "ultra_bomb_item";
        ItemStack bombItem = new ItemStack(Material.TNT);
        ItemMeta meta = bombItem.getItemMeta();
        if (meta != null) {
            String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "Legendary Grenade";
            meta.setDisplayName(displayName);
            String loreDisplayName = displayName.replace(" Grenade", "");
            meta.setLore(Arrays.asList("", ChatColor.WHITE + "Throw this " + ChatColor.GOLD + loreDisplayName, ChatColor.WHITE + "grenade onto your mine", ChatColor.WHITE + "to form a huge explosion!", "", ChatColor.GRAY + "(( Right click to use ))"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(Astra.getPlugin(), NAMESPACE_KEY), PersistentDataType.STRING, ITEM_KEY);

            bombItem.setItemMeta(meta);
        }
        return bombItem;
    }

    public static boolean isBombItem(ItemStack item, String itemKey) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        return itemKey.equals(data.get(new NamespacedKey(Astra.getPlugin(), NAMESPACE_KEY), PersistentDataType.STRING));
    }

}
