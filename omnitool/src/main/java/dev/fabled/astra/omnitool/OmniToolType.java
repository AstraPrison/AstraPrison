package dev.fabled.astra.omnitool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum OmniToolType {

    SWORD,
    PICKAXE,
    AXE,
    SHOVEL,
    HOE;

    public static @Nullable OmniToolType getFromItem(@NotNull final ItemStack item) {
        final Material material = item.getType();
        final String mat = material.toString().toLowerCase();

        if (mat.endsWith("_sword")) {
            return SWORD;
        }

        if (mat.endsWith("_pickaxe")) {
            return PICKAXE;
        }

        if (mat.endsWith("_axe")) {
            return AXE;
        }

        if (mat.endsWith("_shovel")) {
            return SHOVEL;
        }

        if (mat.endsWith("_hoe")) {
            return HOE;
        }

        return null;
    }

}
