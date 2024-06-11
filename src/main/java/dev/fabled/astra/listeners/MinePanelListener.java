package dev.fabled.astra.listeners;


import dev.fabled.astra.menus.MinePanel;
import dev.fabled.astra.mines.generator.MineGenerator;
import dev.fabled.astra.utils.MineWriter;
import dev.fabled.astra.utils.mines.MineChanger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class MinePanelListener implements Listener {


    MinePanel minePanel = new MinePanel();
    MineChanger mineChanger = new MineChanger();

    private Inventory previousInventory;
    private int currentPage = 0;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof MinePanel) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            if (clickedItem.getType() == Material.IRON_PICKAXE && clickedItem.hasItemMeta()) {
                previousInventory = event.getInventory();
                player.openInventory(minePanel.createMineMenu());
            } else if (clickedItem.getType() == Material.REDSTONE_BLOCK && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().replace("Delete ", ""));
                MineWriter.deleteMineFromFile(mineName);
                previousInventory = event.getInventory();
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "The mine \"" + mineName + "\" was deleted.");
            } else if (clickedItem.getType() == Material.TRIPWIRE_HOOK && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().replace("Change airgap of ", ""));
                previousInventory = event.getInventory();
                MineChanger.toggleairgap(mineName);
                player.openInventory(minePanel.createMineConfigurationMenu(mineName));
            } else if (clickedItem.getType() == Material.SPONGE && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().replace("Change luckyblocks of ", ""));
                previousInventory = event.getInventory();
                MineChanger.toggleluckyblocks(mineName);
                player.openInventory(minePanel.createMineConfigurationMenu(mineName));
            } else if (clickedItem.getType() == Material.STONE && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                previousInventory = event.getInventory();
                player.openInventory(minePanel.createMineConfigurationMenu(mineName));
            } else if (clickedItem.getType() == Material.PAPER && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().replace("Change name of ", ""));
                player.sendMessage(ChatColor.YELLOW + "Please enter the new name for the mine \"" + mineName + "\":");
                MineChanger.renameMineMap.put(player, mineName);
                player.closeInventory();
            } else if (clickedItem.getType() == Material.IRON_ORE && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().replace("Change material of ", ""));
                previousInventory = event.getInventory();
                player.openInventory(minePanel.createMaterialConfigurationMenu(mineName, 0));
            } else if (clickedItem.getType() == Material.ANVIL && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().replace("Change reset type of ", ""));
                previousInventory = event.getInventory();
                MineChanger.toggleResetType(mineName);
                player.openInventory(minePanel.createMineConfigurationMenu(mineName));
            } else if (MinePanel.isAllowedMaterial(clickedItem.getType())) {
                String mineName = ChatColor.stripColor(event.getView().getTitle().split(" ")[0]);
                if (event.getClick() == ClickType.LEFT) {
                    if (MineChanger.updateMineMaterialInJson(mineName, clickedItem.getType(), MineChanger.MaterialType.MATERIAL1)) {
                        player.sendMessage(ChatColor.GREEN + "Material1 of mine \"" + mineName + "\" has been updated to " + clickedItem.getType().name() + ".");
                        Collection<BlockState> blockChanges = MineGenerator.getBlocks(player.getUniqueId(), mineName);
                        player.sendBlockChanges(blockChanges);
                        player.openInventory(previousInventory);
                    } else {
                        player.sendMessage(ChatColor.RED + "Failed to update the material. Please try again.");
                    }
                } else if (event.getClick() == ClickType.DROP) {
                    if (MineChanger.updateMineMaterialInJson(mineName, clickedItem.getType(), MineChanger.MaterialType.MATERIAL2)) {
                        player.sendMessage(ChatColor.GREEN + "Material2 of mine \"" + mineName + "\" has been updated to " + clickedItem.getType().name() + ".");
                        Collection<BlockState> blockChanges = MineGenerator.getBlocks(player.getUniqueId(), mineName);
                        player.sendBlockChanges(blockChanges);
                        player.openInventory(previousInventory);
                    } else {
                        player.sendMessage(ChatColor.RED + "Failed to update the material. Please try again.");
                    }
                } else if (event.getClick() == ClickType.RIGHT) {
                    if (MineChanger.updateMineMaterialInJson(mineName, clickedItem.getType(), MineChanger.MaterialType.MATERIAL3)) {
                        player.sendMessage(ChatColor.GREEN + "Material3 of mine \"" + mineName + "\" has been updated to " + clickedItem.getType().name() + ".");
                        Collection<BlockState> blockChanges = MineGenerator.getBlocks(player.getUniqueId(), mineName);
                        player.sendBlockChanges(blockChanges);
                        player.openInventory(previousInventory);
                    } else {
                        player.sendMessage(ChatColor.RED + "Failed to update the material. Please try again.");
                    }
                }
            } else if (clickedItem.getType() == Material.BARRIER) {
                player.openInventory(previousInventory);
            } else if (clickedItem.getType() == Material.ARROW) {
                String title = event.getView().getTitle();
                String[] parts = title.split(" ");
                String mineName = parts[0];
                if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("Previous Page")) {
                    currentPage--;
                    player.openInventory(minePanel.createMaterialConfigurationMenu(mineName, currentPage));
                } else if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("Next Page")) {
                    currentPage++;
                    player.openInventory(minePanel.createMaterialConfigurationMenu(mineName, currentPage));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!MineChanger.renameMineMap.containsKey(player)) {
            return;
        }

        event.setCancelled(true);
        String oldName = MineChanger.renameMineMap.remove(player);
        String newName = event.getMessage();

        if (MineChanger.updateMineNameInJson(oldName, newName)) {
            player.sendMessage(ChatColor.GREEN + "Mine name has been updated to \"" + newName + "\".");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to update the mine name. Please try again.");
        }
    }
}
