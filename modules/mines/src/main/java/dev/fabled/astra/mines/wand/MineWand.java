package dev.fabled.astra.mines.wand;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class MineWand {

    private static MineWand instance;

    public static MineWand getInstance() {
        if (instance == null) {
            instance = new MineWand();
        }

        return instance;
    }

    private final @NotNull NamespacedKey mineWandKey;
    private final @NotNull ItemStack mineWand;

    private MineWand() {
        mineWandKey = new NamespacedKey(Astra.getPlugin(), "astra-mine-wand");

        mineWand = new ItemStack(Material.DIAMOND_AXE);
        final ItemMeta meta = mineWand.getItemMeta();

        meta.displayName(MiniColor.INVENTORY.deserialize("<b><aqua>Mine</b> <b>Wand</b>"));
        meta.lore(MiniColor.INVENTORY.deserialize(List.of(
                "<b><dark_gray>▪</b> <white>Left click<gray> to select position one!",
                "<b><dark_gray>▪</b> <white>Right click<gray> to select position two!"
        )));

        meta.setUnbreakable(true);
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_ENCHANTS
        );

        meta.addEnchant(Enchantment.PROTECTION, 1, true);
        meta.getPersistentDataContainer().set(mineWandKey, PersistentDataType.BYTE, (byte) 1);

        mineWand.setItemMeta(meta);
    }

    public boolean isMineWand(final @Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return false;
        }

        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(mineWandKey, PersistentDataType.BYTE);
    }

    public @NotNull NamespacedKey getMineWandKey() {
        return mineWandKey;
    }

    public @NotNull ItemStack getMineWand() {
        return mineWand.clone();
    }

}
