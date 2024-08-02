package dev.fabled.astra.listener;

import dev.fabled.astra.Astra;
import dev.fabled.astra.omnitool.OmniToolItem;
import dev.fabled.astra.omnitool.menu.EnchantGui;
import dev.fabled.astra.omnitool.utils.EnchantmentData;
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

public class EnchantMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.getHolder() instanceof EnchantGui enchantGui) {
            event.setCancelled(true);

            String menuTitle = ChatColor.stripColor(event.getView().getTitle());
            String formattedMenuTitle = menuTitle.replace("Upgrading ", "").toLowerCase();
            String enchantName = formattedMenuTitle.toLowerCase();

            EnchantmentData enchantmentData = EnchantmentData.enchantments.get(enchantName);

            if (enchantmentData == null) {
                player.sendMessage(ChatColor.RED + "Enchantment data not found for " + enchantName);
                return;
            }

            NamespacedKey enchantKey = new NamespacedKey(Astra.getPlugin(), enchantName);
            NamespacedKey prestigeKey = new NamespacedKey(Astra.getPlugin(), enchantName + "_prestige");

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
                int currentLevel = pdc.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0);
                int maxLevel = enchantmentData.getMaxLevel();

                if (displayName.contains("+1 level")) {
                    int newLevel = Math.min(currentLevel + 1, maxLevel);
                    pdc.set(enchantKey, PersistentDataType.INTEGER, newLevel);
                } else if (displayName.contains("+10 levels")) {
                    int newLevel = Math.min(currentLevel + 10, maxLevel);
                    pdc.set(enchantKey, PersistentDataType.INTEGER, newLevel);
                } else if (displayName.contains("+100 levels")) {
                    int newLevel = Math.min(currentLevel + 100, maxLevel);
                    pdc.set(enchantKey, PersistentDataType.INTEGER, newLevel);
                } else if (displayName.contains("+1k levels")) {
                    int newLevel = Math.min(currentLevel + 1000, maxLevel);
                    pdc.set(enchantKey, PersistentDataType.INTEGER, newLevel);
                } else if (displayName.contains("Max Upgrade")) {
                    pdc.set(enchantKey, PersistentDataType.INTEGER, maxLevel);
                } else if (displayName.contains("Refund levels")) {
                    pdc.remove(enchantKey);
                } else if (displayName.contains("Prestige enchant")) {
                    if (enchantmentData.isPrestigeable()) {
                        int currentPrestige = pdc.getOrDefault(prestigeKey, PersistentDataType.INTEGER, 0);
                        int maxPrestige = enchantmentData.getMaxPrestige();
                        if (currentPrestige < maxPrestige) {
                            player.sendMessage(ChatColor.GOLD + "You have prestiged the enchantment " + enchantName + "!");
                            pdc.set(prestigeKey, PersistentDataType.INTEGER, currentPrestige + 1);
                            pdc.set(enchantKey, PersistentDataType.INTEGER, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You have reached the maximum prestige level for this enchantment.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "This enchantment cannot be prestiged.");
                    }
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
