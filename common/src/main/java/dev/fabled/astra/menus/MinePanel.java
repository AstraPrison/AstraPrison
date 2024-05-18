package dev.fabled.astra.menus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MinePanel implements InventoryHolder, Listener {

    private final Player player;

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
                player.openInventory(createMineMenu());
            }
        }
    }

    private Inventory createMineMenu() {
        final String title = "Astra | Mines";
        Inventory inventory = Bukkit.createInventory(this, 54, MiniColor.INVENTORY.deserialize(title));

        fillInventory(inventory);

        return inventory;
    }

    private void fillInventory(Inventory inventory) {
        File file = new File("plugins/Astra/data/mines.json");
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (int i = 0; i < minesArray.size(); i++) {
                    JsonObject mine = minesArray.get(i).getAsJsonObject();
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
                                ChatColor.GRAY + "Size: " + size));
                        mineItem.setItemMeta(meta);
                        inventory.addItem(mineItem);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatPosition(JsonObject position) {
        if (position.has("startX") && position.has("startY") && position.has("startZ")) {
            int startX = position.get("startX").getAsInt();
            int startY = position.get("startY").getAsInt();
            int startZ = position.get("startZ").getAsInt();

            int endX = position.has("endX") ? position.get("endX").getAsInt() : startX;
            int endY = position.has("endY") ? position.get("endY").getAsInt() : startY;
            int endZ = position.has("endZ") ? position.get("endZ").getAsInt() : startZ;

            return "Start: (" + startX + ", " + startY + ", " + startZ + ") - End: (" + endX + ", " + endY + ", " + endZ + ")";
        } else {
            return "(Unknown)";
        }
    }
}
