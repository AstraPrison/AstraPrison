package dev.fabled.astra.omnitool;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public enum ToolType {

    PICKAXE(Material.NETHERITE_PICKAXE),
    AXE(Material.NETHERITE_AXE),
    SHOVEL(Material.NETHERITE_SHOVEL);

    private final @NotNull Material def;

    ToolType(final @NotNull Material def) {
        this.def = def;
    }

    public @NotNull Material getDef() {
        return def;
    }

}
