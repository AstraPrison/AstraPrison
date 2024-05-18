package dev.fabled.astra.mines.wand;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.MineWriter;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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

    private static final NamespacedKey WAND_NAMESPACED_KEY;
    private static final ItemStack WAND;
    private static final Map<UUID, Location> POSITION_ONE;
    private static final Map<UUID, Location> POSITION_TWO;
    private static final Map<UUID, Boolean> hasRightClicked = new HashMap<>();
    private static final int mineCounter = 0;

    static {
        WAND_NAMESPACED_KEY = new NamespacedKey(Astra.getPlugin(), "astra-mine-wand");

        WAND = new ItemStack(Material.GOLDEN_AXE);
        final ItemMeta meta = WAND.getItemMeta();
        meta.displayName(MiniColor.INVENTORY.deserialize("<b><aqua>Mine</b> <b>Wand"));
        meta.lore(List.of(
                MiniColor.INVENTORY.deserialize("<white>Left click<gray> to select position one!"),
                MiniColor.INVENTORY.deserialize("<white>Right click<gray> to select position two!")
        ));

        meta.getPersistentDataContainer().set(WAND_NAMESPACED_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.addEnchant(Enchantment.PROTECTION, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

        WAND.setItemMeta(meta);

        POSITION_ONE = new HashMap<>();
        POSITION_TWO = new HashMap<>();
    }

    public static @NotNull ItemStack get() {
        return WAND.clone();
    }

    public static void give(@NotNull final Player player) {
        player.getInventory().addItem(WAND.clone());
    }

    public static boolean isWand(@Nullable final ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(WAND_NAMESPACED_KEY, PersistentDataType.BYTE);
    }

    public static void setPositionOne(@NotNull final Player player, @NotNull final Location location) {
        if (!hasPositionOne(player)) {
            POSITION_ONE.put(player.getUniqueId(), location);
        }
    }

    public static void setPositionTwo(@NotNull final Player player, @NotNull final Location location) {
        if (!hasPositionTwo(player)) {
            Location pos1 = getPositionOne(player);
            //if (pos1 != null && checkOverlap(pos1, location)) {
            //    player.sendMessage(MiniColor.parse("<red>Positions overlap! Please select new positions."));
            //   return;
            // }

            POSITION_TWO.put(player.getUniqueId(), location);
            String mineName = generateMineName();
            MineWriter.writeMineToFile(pos1, location, mineName);
            player.sendMessage(MiniColor.parse("<green>The mine with the name \"" + mineName + "\" has been successfully created!"));

            // Setze die Positionen und den Rechtsklick-Status zurück
            POSITION_ONE.remove(player.getUniqueId());
            POSITION_TWO.remove(player.getUniqueId());
            hasRightClicked.remove(player.getUniqueId());
        }
    }

    private static String generateMineName() {
        String baseName = "Mine";
        char letter = 'A';
        //while (isMineNameTaken(baseName + letter)) {
        //    letter++;
        // }
        return baseName + letter;
    }

    //  private static boolean isMineNameTaken(String mineName) {
    //      try {
    //          File file = new File(FILE);
    //          if (!file.exists()) {
    //              return false;
    //          }
    //
    //          FileReader reader = new FileReader(file);
    //          JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
    //          reader.close();
    //
    //          if (jsonObject.has("mines")) {
    //              JsonArray minesArray = jsonObject.getAsJsonArray("mines");
    //              for (int i = 0; i < minesArray.size(); i++) {
    //                  JsonObject mine = minesArray.get(i).getAsJsonObject();
    //                  if (mine.has("name") && mine.get("name").getAsString().equals(mineName)) {
    //                      return true;
    //                  }
    //              }
    //          }
    //      } catch (IOException e) {
    //          e.printStackTrace();
    //      }
    //      return false;
    //  }

    public static boolean hasPositionOne(@NotNull final Player player) {
        return POSITION_ONE.containsKey(player.getUniqueId());
    }

    public static boolean hasPositionTwo(@NotNull final Player player) {
        return POSITION_TWO.containsKey(player.getUniqueId());
    }

    public static Location getPositionOne(@NotNull final Player player) {
        return POSITION_ONE.get(player.getUniqueId());
    }

    public static Location getPositionTwo(@NotNull final Player player) {
        return POSITION_TWO.get(player.getUniqueId());
    }

    public static boolean checkOverlap(@NotNull Location pos1, @NotNull Location pos2) {
        try {
            File file = new File(FILE);
            if (!file.exists()) {
                return false;
            }

            FileReader reader = new FileReader(file);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (int i = 0; i < minesArray.size(); i++) {
                    JsonObject mine = minesArray.get(i).getAsJsonObject();
                    JsonObject pos1Data = mine.getAsJsonObject("pos1");
                    JsonObject pos2Data = mine.getAsJsonObject("pos2");

                    Location existingPos1 = new Location(null, pos1Data.get("startX").getAsDouble(), pos1Data.get("startY").getAsDouble(), pos1Data.get("startZ").getAsDouble());
                    Location existingPos2 = new Location(null, pos2Data.get("endX").getAsDouble(), pos2Data.get("endY").getAsDouble(), pos2Data.get("endZ").getAsDouble());

                    if (positionsOverlap(existingPos1, existingPos2, pos1, pos2)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean positionsOverlap(Location existingPos1, Location existingPos2, Location pos1, Location pos2) {
        return existingPos1.equals(pos1) || existingPos2.equals(pos1) || existingPos1.equals(pos2) || existingPos2.equals(pos2);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        UUID playerId = player.getUniqueId();

        if (isWand(item)) {
            if (event.getAction().name().contains("RIGHT") && hasRightClicked.containsKey(playerId) && hasRightClicked.get(playerId)) {
                return;
            }

            if (event.getHand().name().contains("HAND") && event.getAction().name().contains("RIGHT")) {
                setPositionTwo(player, player.getLocation());
                hasRightClicked.put(playerId, true);
                event.setCancelled(true);
            } else if (event.getAction().name().contains("LEFT")) {
                setPositionOne(player, player.getLocation());
                hasRightClicked.put(playerId, false);
                event.setCancelled(true);
            }
        }
    }
}
