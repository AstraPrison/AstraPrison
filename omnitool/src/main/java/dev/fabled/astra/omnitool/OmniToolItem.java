package dev.fabled.astra.omnitool;

import dev.fabled.astra.Astra;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OmniToolItem {

    private static final NamespacedKey OMNITOOL_KEY = new NamespacedKey(Astra.getPlugin(), "omnitool");
    private static final NamespacedKey OWNER_UUID_KEY = new NamespacedKey(Astra.getPlugin(), "ownerUUID");
    private static final Map<String, String> ENCHANT_DISPLAY_NAMES = Map.of(
            "tokenfinder", "Token Finder",
            "shockwave", "Shockwave",
            "fortune", "Fortune"
            // Hier können weitere Verzauberungen hinzugefügt werden
    );
    private static ItemStack OMNITOOL;

    public static void initialize(NamespacedKey OMNITOOL_NAMESPACED_KEY) {
        OMNITOOL = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = OMNITOOL.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lAstra Omnitool"));
        meta.setLore(createInitialLore());
        meta.getPersistentDataContainer().set(OMNITOOL_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        OMNITOOL.setItemMeta(meta);
    }

    private static List<String> createInitialLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&lSTATISTICS"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Owner: <none>"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Blocks: <none>"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&lENCHANTS"));
        return lore;
    }

    public static ItemStack createOmniToolForPlayer(Player player) {
        ItemStack omniTool = OMNITOOL.clone();
        setOwner(omniTool, player);
        updateLore(omniTool, player.getUniqueId().toString());
        return omniTool;
    }

    public static boolean isOmniTool(@NotNull final ItemStack pickaxe) {
        if (pickaxe.getType() != Material.DIAMOND_PICKAXE) {
            return false;
        }

        final ItemMeta meta = pickaxe.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(OMNITOOL_KEY, PersistentDataType.BYTE);
    }

    public static boolean isOwner(@NotNull final Player player, @NotNull final ItemStack pickaxe) {
        if (pickaxe.getType() != Material.DIAMOND_PICKAXE) {
            return false;
        }

        final ItemMeta meta = pickaxe.getItemMeta();
        if (meta == null) {
            return false;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String ownerUUID = pdc.get(OWNER_UUID_KEY, PersistentDataType.STRING);
        return player.getUniqueId().toString().equals(ownerUUID);
    }

    public static void setOwner(@NotNull final ItemStack pickaxe, @NotNull final Player owner) {
        if (pickaxe.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        final ItemMeta meta = pickaxe.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(OWNER_UUID_KEY, PersistentDataType.STRING, owner.getUniqueId().toString());
        pickaxe.setItemMeta(meta);
    }

    public static void updateLore(@NotNull final ItemStack pickaxe, @NotNull final String ownerUUID) {
        if (pickaxe.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        final ItemMeta meta = pickaxe.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String ownerName = Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)).getName();
        int brokenBlocks = pdc.getOrDefault(new NamespacedKey(Astra.getPlugin(), "brokenBlocks"), PersistentDataType.INTEGER, 0);

        List<String> lore = createInitialLore();
        lore.set(2, ChatColor.translateAlternateColorCodes('&', "&f| Owner: &a" + (ownerName != null ? ownerName : "Unknown")));
        lore.set(3, ChatColor.translateAlternateColorCodes('&', "&f| Blocks: &a" + brokenBlocks));

        for (Map.Entry<String, String> entry : ENCHANT_DISPLAY_NAMES.entrySet()) {
            String enchantKey = entry.getKey();
            String enchantDisplayName = entry.getValue();
            int enchantLevel = pdc.getOrDefault(new NamespacedKey(Astra.getPlugin(), enchantKey), PersistentDataType.INTEGER, 0);
            boolean isDisabled = pdc.has(new NamespacedKey(Astra.getPlugin(), "disabled_" + enchantKey), PersistentDataType.INTEGER);

            if (enchantLevel > 0) {
                String enchantText = "&d| &f" + enchantDisplayName + ": &a" + enchantLevel;
                if (isDisabled) {
                    enchantText += ChatColor.RED + " (Disabled)";
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', enchantText));
            }
        }

        meta.setLore(lore);
        pickaxe.setItemMeta(meta);
    }


    public static void addBrokenBlock(@NotNull final ItemStack pickaxe) {
        if (pickaxe.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        final ItemMeta meta = pickaxe.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        int brokenBlocks = pdc.getOrDefault(new NamespacedKey(Astra.getPlugin(), "brokenBlocks"), PersistentDataType.INTEGER, 0);
        pdc.set(new NamespacedKey(Astra.getPlugin(), "brokenBlocks"), PersistentDataType.INTEGER, brokenBlocks + 1);
        pickaxe.setItemMeta(meta);
    }

    public static int getBrokenBlocks(@NotNull final ItemStack pickaxe) {
        if (pickaxe.getType() != Material.DIAMOND_PICKAXE) {
            return 0;
        }

        final ItemMeta meta = pickaxe.getItemMeta();
        if (meta == null) {
            return 0;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.getOrDefault(new NamespacedKey(Astra.getPlugin(), "brokenBlocks"), PersistentDataType.INTEGER, 0);
    }
}
