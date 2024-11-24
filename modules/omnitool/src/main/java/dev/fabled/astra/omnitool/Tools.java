package dev.fabled.astra.omnitool;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum Tools {

    PICKAXE(Material.NETHERITE_PICKAXE),
    AXE(Material.NETHERITE_AXE),
    SHOVEL(Material.NETHERITE_SHOVEL);

    private final @NotNull Material def;

    Tools(final @NotNull Material def) {
        this.def = def;
    }

    public static Material getPreferredTool(final @NotNull Block block) {
        for (final Tools tool : Tools.values()) {
            final ItemStack item = new ItemStack(tool.def);
            if (block.isPreferredTool(item)) {
                return tool.def;
            }
        }

        return Material.NETHERITE_PICKAXE;
    }

    public @NotNull Material getDef() {
        return def;
    }

}
