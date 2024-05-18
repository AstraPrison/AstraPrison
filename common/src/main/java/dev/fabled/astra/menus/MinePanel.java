package dev.fabled.astra.menus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.AllowedMaterials;
import dev.fabled.astra.utils.mines.MineChanger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MinePanel implements InventoryHolder, Listener {

    private final Player player;
    private Inventory previousInventory;
    private int currentPage = 0;

    public MinePanel(Player player) {
        this.player = player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        final String title = "Astra | Mines Panel";
        Inventory inventory = Bukkit.createInventory(this, 27, MiniColor.INVENTORY.deserialize(title));

        ItemStack item = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Open Mines Menu");
        item.setItemMeta(meta);
        inventory.setItem(13, item);

        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof MinePanel) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            if (clickedItem.getType() == Material.IRON_PICKAXE && clickedItem.hasItemMeta()) {
                previousInventory = event.getInventory();
                player.openInventory(createMineMenu());
            } else if (clickedItem.getType() == Material.STONE && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                previousInventory = event.getInventory();
                player.openInventory(createMineConfigurationMenu(mineName));
            } else if (clickedItem.getType() == Material.PAPER && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().replace("Change name of ", ""));
                player.sendMessage(ChatColor.YELLOW + "Please enter the new name for the mine \"" + mineName + "\":");
                MineChanger.renameMineMap.put(player, mineName);
                player.closeInventory();
            } else if (clickedItem.getType() == Material.IRON_ORE && clickedItem.hasItemMeta()) {
                String mineName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().replace("Change material of ", ""));
                previousInventory = event.getInventory();
                player.openInventory(createMaterialConfigurationMenu(mineName, 0));
            } else if (isExcludedMaterial(clickedItem.getType())) {
                String mineName = ChatColor.stripColor(event.getView().getTitle().split(" ")[0]);
                if (MineChanger.updateMineMaterialInJson(mineName, clickedItem.getType())) {
                    player.sendMessage(ChatColor.GREEN + "Material of mine \"" + mineName + "\" has been updated to " + clickedItem.getType().name() + ".");
                    player.openInventory(previousInventory);
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to update the material. Please try again.");
                }
            } else if (clickedItem.getType() == Material.BARRIER) {
                player.openInventory(previousInventory);
            } else if (clickedItem.getType() == Material.ARROW) {
                String title = event.getView().getTitle();
                String[] parts = title.split(" ");
                String mineName = parts[0];
                if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("Previous Page")) {
                    currentPage--;
                    player.openInventory(createMaterialConfigurationMenu(mineName, currentPage));
                } else if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("Next Page")) {
                    currentPage++;
                    player.openInventory(createMaterialConfigurationMenu(mineName, currentPage));
                }
            }
        }
    }





    private Inventory createMineMenu() {
        final String title = "Astra | Mines";
        Inventory inventory = Bukkit.createInventory(this, 54, MiniColor.INVENTORY.deserialize(title));

        fillInventory(inventory);

        addBackButton(inventory, 53);

        return inventory;
    }

    private Inventory createMineConfigurationMenu(String mineName) {
        final String title = mineName + " Configuration";
        Inventory inventory = Bukkit.createInventory(this, 27, MiniColor.INVENTORY.deserialize(title));

        ItemStack configItem = new ItemStack(Material.PAPER);
        ItemMeta meta = configItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Change name of " + mineName);
        meta.setLore(List.of(ChatColor.GRAY + "(( Click to change the name of the mine ))"));
        configItem.setItemMeta(meta);
        ItemStack materialItem = new ItemStack(Material.IRON_ORE);
        meta = materialItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Change material of " + mineName);
        meta.setLore(List.of(ChatColor.GRAY + "(( Click to change the material of the mine ))"));
        materialItem.setItemMeta(meta);
        inventory.setItem(10, configItem);
        inventory.setItem(12, materialItem);

        addBackButton(inventory, 26);

        return inventory;
    }

    private Inventory createMaterialConfigurationMenu(String mineName, int page) {
        final String title = mineName + " Materials (Page " + (page + 1) + ")";
        Inventory inventory = Bukkit.createInventory(this, 54, MiniColor.INVENTORY.deserialize(title));

        Material[] materials = Material.values();
        int itemsPerPage = 45;
        int startIndex = page * itemsPerPage;
        int endIndex = materials.length;

        int count = 0;

        for (int i = startIndex; i < endIndex && count < itemsPerPage; i++) {
            Material material = materials[i];
            if (isExcludedMaterial(material)) {
                ItemStack itemStack = new ItemStack(material);
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.GREEN + material.name());
                    itemStack.setItemMeta(meta);
                    inventory.addItem(itemStack);
                    count++;
                }
            }
        }

        int totalPages = (int) Math.ceil((double) countMaterials() / itemsPerPage);
        addNavigationButtons(inventory, page, totalPages);
        addBackButton(inventory, 49);

        return inventory;
    }

    private boolean isExcludedMaterial(Material material) {
        AllowedMaterials allowedMaterials = Astra.getAllowedMaterials();
        if (allowedMaterials != null) {
            return allowedMaterials.isAllowed(material);
        } else {
            player.sendMessage(ChatColor.RED + "Failed to load the allowed materials. Please try again.");
            return false;
        }
    }

    private int countMaterials() {
        Material[] materials = Material.values();
        int count = 0;
        for (Material material : materials) {
            if (isExcludedMaterial(material)) {
                count++;
            }
        }
        return count;
    }


    private void addNavigationButtons(Inventory inventory, int currentPage, int totalPages) {
        if (currentPage > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName(ChatColor.YELLOW + "Previous Page");
            prevPage.setItemMeta(prevMeta);
            inventory.setItem(48, prevPage);
        }

        if (currentPage < totalPages - 1) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName(ChatColor.YELLOW + "Next Page");
            nextPage.setItemMeta(nextMeta);
            inventory.setItem(50, nextPage);
        }
    }


    private void addBackButton(Inventory inventory, int slot) {
        ItemStack backItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = backItem.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Back");
        backItem.setItemMeta(meta);
        inventory.setItem(slot, backItem);
    }

    private void fillInventory(Inventory inventory) {
        File file = new File("plugins/Astra/data/mines.json");
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (int i = 0; i < minesArray.size(); i++) {
                    JsonElement mineElement = minesArray.get(i);
                    if (mineElement.isJsonObject()) {
                        JsonObject mine = mineElement.getAsJsonObject();
                        if (mine.has("name") && mine.has("pos1") && mine.has("pos2")) {
                            String mineName = mine.get("name").getAsString();
                            JsonObject pos1 = mine.getAsJsonObject("pos1");
                            JsonObject pos2 = mine.getAsJsonObject("pos2");
                            int sizeX = Math.abs(pos1.get("startX").getAsInt() - pos2.get("endX").getAsInt()) + 1;
                            int sizeY = Math.abs(pos1.get("startY").getAsInt() - pos2.get("endY").getAsInt()) + 1;
                            int sizeZ = Math.abs(pos1.get("startZ").getAsInt() - pos2.get("endZ").getAsInt()) + 1;
                            String size = sizeX + "x" + sizeY + "x" + sizeZ;


                            ItemStack mineItem = new ItemStack(Material.STONE);
                            ItemMeta meta = mineItem.getItemMeta();
                            meta.setDisplayName(ChatColor.GREEN + mineName);
                            meta.setLore(Arrays.asList(
                                    ChatColor.GRAY + "Position 1: " + formatPosition(pos1),
                                    ChatColor.GRAY + "Position 2: " + formatPosition(pos2),
                                    ChatColor.GRAY + "Size: " + size,
                                    ChatColor.WHITE + "Click to configure this mine"));
                            mineItem.setItemMeta(meta);
                            inventory.addItem(mineItem);
                        }
                    } else {
                        System.err.println("Element at index " + i + " is not a JsonObject.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String formatPosition(JsonObject position) {
        int x = position.has("startX") ? position.get("startX").getAsInt() : position.get("endX").getAsInt();
        int y = position.has("startY") ? position.get("startY").getAsInt() : position.get("endY").getAsInt();
        int z = position.has("startZ") ? position.get("startZ").getAsInt() : position.get("endZ").getAsInt();

        return "(" + x + ", " + y + ", " + z + ")";
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
