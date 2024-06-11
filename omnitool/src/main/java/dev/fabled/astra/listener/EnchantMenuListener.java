package dev.fabled.astra.listener;

import dev.fabled.astra.Astra;
import dev.fabled.astra.omnitool.OmniToolItem;
import dev.fabled.astra.omnitool.menu.EnchantGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class EnchantMenuListener implements Listener {

    private final Map<String, NamespacedKey> enchantmentKeys;

    public EnchantMenuListener() {
        enchantmentKeys = new HashMap<>();
        enchantmentKeys.put("tokenfinder", new NamespacedKey(Astra.getPlugin(), "tokenfinder"));
        enchantmentKeys.put("shockwave", new NamespacedKey(Astra.getPlugin(), "shockwave"));
        enchantmentKeys.put("fortune", new NamespacedKey(Astra.getPlugin(), "fortune"));
        Astra.getPlugin().getLogger().info("Enchantment menu listener initialized!");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.getHolder() instanceof EnchantGui) {
            event.setCancelled(true);

            String menuTitle = ChatColor.stripColor(event.getView().getTitle());
            String formatedMenuTitle = menuTitle.replace("Upgrading ", "");
            String enchantName = formatedMenuTitle;
            NamespacedKey enchantKey = enchantmentKeys.get(enchantName.toLowerCase());

            if (enchantKey == null) return;

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            ItemMeta itemMeta = clickedItem.getItemMeta();
            if (itemMeta == null) return;

            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand == null || itemInHand.getType() != Material.DIAMOND_PICKAXE) return;

            ItemMeta itemInHandMeta = itemInHand.getItemMeta();
            if (itemInHandMeta == null) return;

            PersistentDataContainer pdc = itemInHandMeta.getPersistentDataContainer();

            if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                if (displayName.contains("+1 level")) {
                    int currentLevel = pdc.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0);
                    pdc.set(enchantKey, PersistentDataType.INTEGER, currentLevel + 1);
                } else if (displayName.contains("+10 levels")) {
                    int currentLevel = pdc.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0);
                    pdc.set(enchantKey, PersistentDataType.INTEGER, currentLevel + 10);
                } else if (displayName.contains("+100 levels")) {
                    int currentLevel = pdc.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0);
                    pdc.set(enchantKey, PersistentDataType.INTEGER, currentLevel + 100);
                } else if (displayName.contains("+1k levels")) {
                    int currentLevel = pdc.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0);
                    pdc.set(enchantKey, PersistentDataType.INTEGER, currentLevel + 1000);
                } else if (displayName.contains("Max Upgrade")) {
                    pdc.set(enchantKey, PersistentDataType.INTEGER, Integer.MAX_VALUE);
                } else if (displayName.contains("Refund levels")) {
                    pdc.remove(enchantKey);
                } else if (displayName.contains("Prestige enchant")) {
                    // PRESTIGE TODO
                } else if (displayName.contains("Toggle enchant")) {
                    NamespacedKey disabledKey = new NamespacedKey(Astra.getPlugin(), "disabled_" + enchantName.toLowerCase());
                    if (pdc.has(disabledKey, PersistentDataType.INTEGER)) {
                        pdc.remove(disabledKey);
                        player.sendMessage(ChatColor.GREEN + "Enchant " + enchantName + " enabled!");
                    } else {
                        pdc.set(disabledKey, PersistentDataType.INTEGER, 1);
                        player.sendMessage(ChatColor.RED + "Enchant " + enchantName + " disabled!");
                    }
                    OmniToolItem.updateLore(itemInHand, player.getUniqueId().toString());
                }
            }

            itemInHand.setItemMeta(itemInHandMeta);

            OmniToolItem.updateLore(itemInHand, player.getUniqueId().toString());
            player.updateInventory();
        }
    }


}