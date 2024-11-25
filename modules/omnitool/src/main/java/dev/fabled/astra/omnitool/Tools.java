package dev.fabled.astra.omnitool;

import dev.fabled.astra.nms.AbstractNMSHandler;
import dev.fabled.astra.nms.NMSFactory;
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

    public static @NotNull Material getPreferredTool(final @NotNull Material material) {
        final AbstractNMSHandler nmsHandler = NMSFactory.getNMSHandler();
        assert nmsHandler != null;

        for (final Tools tool : Tools.values()) {
            final ItemStack item = new ItemStack(tool.def);
            if (nmsHandler.canBreakMaterial(item, material)) {
                return tool.def;
            }
        }

        return Material.NETHERITE_PICKAXE;
    }

    public static @NotNull Material getPreferredTool(final @NotNull Block block) {
        return getPreferredTool(block.getType());
    }

    public @NotNull Material getDef() {
        return def;
    }

}
