package dev.fabled.astra.menus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.AllowedMaterials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class MinePanel implements InventoryHolder {

    private final Inventory inventory;

    public MinePanel() {
        this.inventory = createMinePanel();
    }

    public static boolean isAllowedMaterial(Material material) {
        AllowedMaterials allowedMaterials = Astra.getAllowedMaterials();
        if (allowedMaterials != null) {
            return allowedMaterials.isAllowed(material);
        } else {
            System.out.println(ChatColor.RED + "Failed to load the allowed materials. Please try again.");
            return false;
        }
    }

    private static void addNavigationButtons(Inventory inventory, int currentPage, int totalPages) {
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

    private static void addBackButton(Inventory inventory, int slot) {
        ItemStack backItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = backItem.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Back");
        backItem.setItemMeta(meta);
        inventory.setItem(slot, backItem);
    }

    private static void fillInventory(Inventory inventory) {
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

    private static String formatPosition(JsonObject position) {
        int x = position.has("startX") ? position.get("startX").getAsInt() : position.get("endX").getAsInt();
        int y = position.has("startY") ? position.get("startY").getAsInt() : position.get("endY").getAsInt();
        int z = position.has("startZ") ? position.get("startZ").getAsInt() : position.get("endZ").getAsInt();

        return "(" + x + ", " + y + ", " + z + ")";
    }

    private Inventory createMinePanel() {
        final String title = "Astra | Mines Panel";
        Inventory inv = Bukkit.createInventory(this, 27, MiniColor.INVENTORY.deserialize(title));

        ItemStack item = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Open Mines Menu");
        item.setItemMeta(meta);
        inv.setItem(13, item);

        return inv;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public Inventory createMineMenu() {
        final String title = "Astra | Mines";
        Inventory inventory = Bukkit.createInventory(this, 54, MiniColor.INVENTORY.deserialize(title));

        fillInventory(inventory);

        addBackButton(inventory, 53);

        return inventory;
    }

    public Inventory createMineConfigurationMenu(String mineName) {
        final String title = mineName + " Configuration";
        File file = new File("plugins/Astra/data/mines.json");
        Inventory inventory = Bukkit.createInventory(MinePanel.this, 27, MiniColor.INVENTORY.deserialize(title));


        ItemStack configItem = new ItemStack(Material.PAPER);
        ItemMeta meta = configItem.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Change name of " + mineName);
        meta.setLore(List.of(ChatColor.GRAY + "(( Click to change the name of the mine ))"));
        configItem.setItemMeta(meta);
        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            if (jsonObject.has("mines") && jsonObject.get("mines").isJsonArray()) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                String resetType = "Unknown";
                Boolean airgap = true;
                Boolean luckyblocks = true;

                for (JsonElement mineElement : minesArray) {
                    JsonObject mineObject = mineElement.getAsJsonObject();
                    if (mineObject.has("name") && mineObject.get("name").getAsString().equals(mineName)) {
                        if (mineObject.has("resetType")) {
                            resetType = mineObject.get("resetType").getAsString();
                        }
                        if (mineObject.has("airgap")) {
                            airgap = mineObject.get("airgap").getAsBoolean();
                        }
                        if (mineObject.has("luckyblocks")) {
                            luckyblocks = mineObject.get("luckyblocks").getAsBoolean();
                        }
                        break;
                    }
                }

                ItemStack resetItem = new ItemStack(Material.ANVIL);
                ItemMeta resetMeta = resetItem.getItemMeta();
                resetMeta.setDisplayName(ChatColor.RED + "Change reset type of " + mineName);
                resetMeta.setLore(List.of(
                        ChatColor.GRAY + "Current reset type: " + ChatColor.GREEN + resetType,
                        ChatColor.GRAY + "",
                        ChatColor.WHITE + "(( Click to change the reset type of the mine ))"));
                resetItem.setItemMeta(resetMeta);
                inventory.setItem(14, resetItem);
                ItemStack airgapItem = new ItemStack(Material.TRIPWIRE_HOOK);
                ItemMeta airgapmeta = airgapItem.getItemMeta();
                airgapmeta.setDisplayName(ChatColor.GREEN + "Change airgap of " + mineName);
                airgapmeta.setLore(List.of(
                        ChatColor.GRAY + "Current status: " + ChatColor.GREEN + airgap,
                        ChatColor.GRAY + "",
                        ChatColor.WHITE + "(( Click to change the airgap of the mine ))"));
                airgapItem.setItemMeta(airgapmeta);
                inventory.setItem(17, airgapItem);
                ItemStack luckyblockItem = new ItemStack(Material.SPONGE);
                ItemMeta luckyblockmeta = luckyblockItem.getItemMeta();
                luckyblockmeta.setDisplayName(ChatColor.GREEN + "Change luckyblocks of " + mineName);
                luckyblockmeta.setLore(List.of(
                        ChatColor.GRAY + "Current status: " + ChatColor.GREEN + luckyblocks,
                        ChatColor.GRAY + "",
                        ChatColor.WHITE + "(( Click to change ))"));
                luckyblockItem.setItemMeta(luckyblockmeta);
                inventory.setItem(16, luckyblockItem);
            } else {
                throw new RuntimeException("Invalid JSON structure.");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ItemStack materialItem = new ItemStack(Material.IRON_ORE);
        meta = materialItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Change material of " + mineName);
        meta.setLore(List.of(ChatColor.GRAY + "(( Click to change the material of the mine ))"));
        materialItem.setItemMeta(meta);
        ItemStack deleteItem = new ItemStack(Material.REDSTONE_BLOCK);
        meta = deleteItem.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Delete " + mineName);
        meta.setLore(List.of(ChatColor.GRAY + "(( Click to delete the mine ))"));
        deleteItem.setItemMeta(meta);
        inventory.setItem(8, deleteItem);
        inventory.setItem(10, configItem);
        inventory.setItem(12, materialItem);

        addBackButton(inventory, 26);

        return inventory;
    }

    public Inventory createMaterialConfigurationMenu(String mineName, int page) {
        final String title = mineName + " Materials (Page " + (page + 1) + ")";
        Inventory inventory = Bukkit.createInventory(this, 54, MiniColor.INVENTORY.deserialize(title));

        List<Material> allowedMaterials = Astra.getAllowedMaterials().getMaterials();
        int itemsPerPage = 45;
        int startIndex = page * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, allowedMaterials.size());

        for (int i = startIndex; i < endIndex; i++) {
            Material material = allowedMaterials.get(i);
            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + material.name());
                List<String> lore = Arrays.asList(
                        ChatColor.GREEN + "Left click change Material1",
                        ChatColor.YELLOW + "Drop key change Material2",
                        ChatColor.RED + "Right click change Material3"
                );
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.addItem(itemStack);
            }
        }

        int totalPages = (int) Math.ceil((double) allowedMaterials.size() / itemsPerPage);
        addNavigationButtons(inventory, page, totalPages);
        addBackButton(inventory, 49);

        return inventory;
    }
}
