package dev.fabled.astra.mines.wand;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.fabled.astra.utils.MineWriter.FILE;

public class MineWand implements Listener {

    public static final Map<UUID, Location> POSITION_ONE = new HashMap<>();
    public static final Map<UUID, Location> POSITION_TWO = new HashMap<>();
    private static NamespacedKey WAND_NAMESPACED_KEY;
    private static ItemStack WAND;

    public static void initialize(NamespacedKey key) {
        WAND_NAMESPACED_KEY = key;
        WAND = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta = WAND.getItemMeta();
        meta.displayName(MiniColor.INVENTORY.deserialize("<b><aqua>Mine</b> <b>Wand"));
        meta.lore(List.of(
                MiniColor.INVENTORY.deserialize("<white>Left click<gray> to select position one!"),
                MiniColor.INVENTORY.deserialize("<white>Right click<gray> to select position two!")
        ));
        meta.getPersistentDataContainer().set(WAND_NAMESPACED_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        WAND.setItemMeta(meta);
    }

    public static @NotNull ItemStack get() {
        return WAND.clone();
    }

    public static void give(@NotNull Player player) {
        player.getInventory().addItem(WAND.clone());
    }

    public static boolean isWand(@Nullable ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(WAND_NAMESPACED_KEY, PersistentDataType.BYTE);
    }

    public static boolean hasPositionOne(@NotNull Player player) {
        return POSITION_ONE.containsKey(player.getUniqueId());
    }

    public static boolean hasPositionTwo(@NotNull Player player) {
        return POSITION_TWO.containsKey(player.getUniqueId());
    }

    public static Location getPositionOne(@NotNull Player player) {
        return POSITION_ONE.get(player.getUniqueId());
    }

    public static Location getPositionTwo(@NotNull Player player) {
        return POSITION_TWO.get(player.getUniqueId());
    }

    public static String generateMineName() {
        String baseName = "Mine";
        char letter = 'A';
        while (isMineNameTaken(baseName + letter)) letter++;
        return baseName + letter;
    }

    public static boolean isMineNameTaken(String mineName) {
        try {
            File file = new File(FILE);
            if (!file.exists()) return false;
            FileReader reader = new FileReader(file);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (int i = 0; i < minesArray.size(); i++) {
                    JsonObject mine = minesArray.get(i).getAsJsonObject();
                    if (mine.has("name") && mine.get("name").getAsString().equals(mineName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!isWand(item)) {
            return;
        }

        UUID playerId = player.getUniqueId();

        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                if (event.getHand() == EquipmentSlot.OFF_HAND) {
                    return;
                }
                if (hasPositionOne(player)) {
                    player.sendMessage(MiniColor.parse("<red>Position one already set!"));
                } else {
                    POSITION_ONE.put(playerId, player.getLocation());
                    player.sendMessage(MiniColor.parse("<green>Position one set!"));
                }
                event.setCancelled(true);
                break;

            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                if (event.getHand() == EquipmentSlot.OFF_HAND) {
                    return;
                }
                if (hasPositionTwo(player)) {
                    player.sendMessage(MiniColor.parse("<red>Position two already set!"));
                } else {
                    POSITION_TWO.put(playerId, player.getLocation());
                    player.sendMessage(MiniColor.parse("<green>Position two set!"));
                    player.sendMessage(MiniColor.parse("<yellow>Both positions set! Type '/mineadmin create <name>' to generate a mine!"));
                }
                event.setCancelled(true);
                break;

            default:
                break;
        }
    }
}
