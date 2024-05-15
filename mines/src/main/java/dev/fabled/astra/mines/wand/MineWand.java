package dev.fabled.astra.mines.wand;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.items.ItemStackUtils;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MineWand {

    private static final NamespacedKey WAND_NAMESPACED_KEY;
    private static final ItemStack WAND;
    private static final Map<UUID, BlockPosition> POSITION_ONE;
    private static final Map<UUID, BlockPosition> POSITION_TWO;

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
        return ItemStackUtils.copy(WAND);
    }

    public static void give(@NotNull final Player player) {
        player.getInventory().addItem(WAND);
    }

    public static boolean isWand(@Nullable final ItemStack item) {
        if (item == null) {
            return false;
        }

        if (item.getType() != Material.GOLDEN_AXE) {
            return false;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(WAND_NAMESPACED_KEY, PersistentDataType.BYTE);
    }

    public static void setPositionOne(@NotNull final Player player, @NotNull final Location location) {
        POSITION_ONE.put(player.getUniqueId(), new BlockPosition(location));
    }

    public static void setPositionTwo(@NotNull final Player player, @NotNull final Location location) {
        POSITION_TWO.put(player.getUniqueId(), new BlockPosition(location));
    }

    public static @Nullable BlockPosition getPositionOne(@NotNull final Player player) {
        return POSITION_ONE.getOrDefault(player.getUniqueId(), null);
    }

    public static @Nullable BlockPosition getPositionTwo(@NotNull final Player player) {
        return POSITION_TWO.getOrDefault(player.getUniqueId(), null);
    }

}
