package dev.fabled.astra.utils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemStackUtils {

    public static @NotNull final Material DEFAULT_MATERIAL;

    static {
        DEFAULT_MATERIAL = Material.STONE;
    }

    public static @NotNull ItemStack getDefaultItemStack() {
        return new ItemStack(DEFAULT_MATERIAL);
    }

    public static @NotNull ItemStack copy(@NotNull final ItemStack item) {
        final ItemStack newItem = new ItemStack(item.getType());
        newItem.setItemMeta(item.getItemMeta());
        return newItem;
    }

}
