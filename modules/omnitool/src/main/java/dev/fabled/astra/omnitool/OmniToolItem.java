package dev.fabled.astra.omnitool;

import dev.fabled.astra.Astra;
import dev.fabled.astra.omnitool.utils.EnchantmentData;
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
    private static final NamespacedKey OWNER_UUID_KEY = new NamespacedKey(Astra.getPlugin(), "omnitool-ownerUUID");
    public static final NamespacedKey LEVEL_KEY = new NamespacedKey(Astra.getPlugin(), "omnitool-level");
    public static final NamespacedKey BLOCKS_KEY = new NamespacedKey(Astra.getPlugin(), "omnitool-brokenBlocks");
    public static final NamespacedKey LEVEL_BLOCKS_KEY = new NamespacedKey(Astra.getPlugin(), "omnitool-levelBlocks");

    private static ItemStack OMNITOOL;

    public static void initialize(NamespacedKey OMNITOOL_NAMESPACED_KEY) {
        OMNITOOL = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = OMNITOOL.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lAstra Omnitool"));
        //meta.setLore(createInitialLore());
        pdc.set(OMNITOOL_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        OMNITOOL.setItemMeta(meta);
    }

    public static ItemStack createOmniToolForPlayer(Player player) {
        ItemStack omniTool = OMNITOOL.clone();
        setOwner(omniTool, player);
        addStartingEnchantments(omniTool);
        updateLore(omniTool, player.getUniqueId().toString());
        return omniTool;
    }

    public static void addStartingEnchantments(ItemStack item) {
        if (item.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();

        Map<String, EnchantmentData> enchantments = EnchantmentData.enchantments;
        for (Map.Entry<String, EnchantmentData> entry : enchantments.entrySet()) {
            String enchantKey = entry.getKey();
            EnchantmentData enchantmentData = entry.getValue();
            int startingLevel = enchantmentData.getStartingLevel();
            if (startingLevel > 0) {
                pdc.set(new NamespacedKey(Astra.getPlugin(), enchantKey), PersistentDataType.INTEGER, startingLevel);
            }
        }
        item.setItemMeta(meta);
    }


    public static boolean isOmniTool(@NotNull final ItemStack item) {
        if (item.getType() != Material.DIAMOND_PICKAXE) {
            return false;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(OMNITOOL_KEY, PersistentDataType.BYTE);
    }

    public static boolean isOwner(@NotNull final Player player, @NotNull final ItemStack item) {
        if (item.getType() != Material.DIAMOND_PICKAXE) {
            return false;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String ownerUUID = pdc.get(OWNER_UUID_KEY, PersistentDataType.STRING);
        return player.getUniqueId().toString().equals(ownerUUID);
    }

    public static void setOwner(@NotNull final ItemStack item, @NotNull final Player owner) {
        if (item.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(OWNER_UUID_KEY, PersistentDataType.STRING, owner.getUniqueId().toString());
        item.setItemMeta(meta);
    }


    public static void updateLore(@NotNull final ItemStack item, @NotNull final String ownerUUID) {
        if (item.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String ownerName = Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)).getName();
        int brokenBlocks = pdc.getOrDefault(BLOCKS_KEY, PersistentDataType.INTEGER, 0);
        int level = pdc.getOrDefault(LEVEL_KEY, PersistentDataType.INTEGER, 0);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&lSTATISTICS:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Owner: &a" + (ownerName != null ? ownerName : "Unknown")));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Blocks: &a" + brokenBlocks));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Level: &a" + level));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&lENCHANTMENTS:"));

        Map<String, EnchantmentData> enchantments = EnchantmentData.enchantments;
        for (Map.Entry<String, EnchantmentData> entry : enchantments.entrySet()) {
            String enchantKey = entry.getKey();
            EnchantmentData enchantmentData = entry.getValue();
            String enchantDisplayName = enchantmentData.getName();
            int enchantLevel = pdc.getOrDefault(new NamespacedKey(Astra.getPlugin(), enchantKey), PersistentDataType.INTEGER, 0);
            boolean isDisabled = pdc.has(new NamespacedKey(Astra.getPlugin(), "disabled_" + enchantKey), PersistentDataType.INTEGER);

            if (enchantLevel > 0) {
                int maxLevel = enchantmentData.getMaxLevel();
                int prestigeLevel = pdc.getOrDefault(new NamespacedKey(Astra.getPlugin(), enchantKey + "_prestige"), PersistentDataType.INTEGER, 0);
                StringBuilder enchantText = new StringBuilder("&d| &f" + enchantDisplayName + ": &a" + enchantLevel);

                if (enchantmentData.isPrestigeable() && prestigeLevel > 0) {
                    int maxPrestige = enchantmentData.getMaxPrestige();
                    if (prestigeLevel > maxPrestige) {
                        prestigeLevel = maxPrestige;
                    }
                    enchantText.append(ChatColor.GOLD).append(" ").append("\u2605".repeat(prestigeLevel));
                }
                if (enchantLevel >= maxLevel) {
                    enchantText.append(ChatColor.YELLOW).append(" (max)");
                }
                if (isDisabled) {
                    enchantText.append(ChatColor.RED).append(" (Disabled)");
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', enchantText.toString()));
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void addBrokenBlock(@NotNull final ItemStack item) {
        if (item.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        int brokenBlocks = pdc.getOrDefault(BLOCKS_KEY, PersistentDataType.INTEGER, 0);
        int levelBlocks = pdc.getOrDefault(LEVEL_BLOCKS_KEY, PersistentDataType.INTEGER, 0);
        pdc.set(BLOCKS_KEY, PersistentDataType.INTEGER, brokenBlocks + 1);
        pdc.set(LEVEL_BLOCKS_KEY, PersistentDataType.INTEGER, levelBlocks + 1);
        item.setItemMeta(meta);
        updateLore(item, pdc.get(OWNER_UUID_KEY, PersistentDataType.STRING));
    }

    public static void addEnchantment(@NotNull final ItemStack item, @NotNull final String enchantKey, int level) {
        if (item.getType() != Material.DIAMOND_PICKAXE) {
            return;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(Astra.getPlugin(), enchantKey), PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);
        updateLore(item, pdc.get(OWNER_UUID_KEY, PersistentDataType.STRING));
    }

    public static int getBrokenBlocks(@NotNull final ItemStack item) {
        if (item.getType() != Material.DIAMOND_PICKAXE) {
            return 0;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return 0;
        }

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.getOrDefault(BLOCKS_KEY, PersistentDataType.INTEGER, 0);
    }

    private static List<String> createInitialLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&lSTATISTICS:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Owner:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Blocks:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f| Level:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&lENCHANTMENTS:"));
        return lore;
    }
}
