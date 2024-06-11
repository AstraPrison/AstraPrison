package dev.fabled.astra.omnitool.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OmnitoolMenu implements InventoryHolder, Listener {

    private final Inventory inventory;
    private final Map<Material, String> enchantments;

    public OmnitoolMenu() {
        this.inventory = Bukkit.createInventory(this, 54, "Omnitool Inventory");
        this.enchantments = new HashMap<>();
        initializeItems();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private void initializeItems() {
        addItemToInventory(Material.SUNFLOWER, "tokenfinder", "tokenfinder");
        addItemToInventory(Material.ANVIL, "shockwave", "shockwave");
        addItemToInventory(Material.EMERALD, "fortune", "fortune");
    }

    private void addItemToInventory(Material material, String displayName, String enchantName) {
        ItemStack item = createGuiItem(material, displayName);
        inventory.addItem(item);
        enchantments.put(material, enchantName);
    }

    private ItemStack createGuiItem(Material material, String name) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
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
        String enchantName = enchantments.get(clickedItem.getType());

        if (enchantName != null) {
            EnchantGui enchantGui = new EnchantGui(player, enchantName);
            player.openInventory(enchantGui.getInventory());
        }
        event.setCancelled(true);
    }
}
