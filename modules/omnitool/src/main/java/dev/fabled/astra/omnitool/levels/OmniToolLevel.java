package dev.fabled.astra.omnitool.levels;

import dev.fabled.astra.omnitool.Tools;
import org.jetbrains.annotations.NotNull;

public final class OmniToolLevel {

    private final @NotNull OmniToolType pickaxe;
    private final @NotNull OmniToolType axe;
    private final @NotNull OmniToolType shovel;

    public OmniToolLevel(final int level) {
        pickaxe = new OmniToolType(Tools.PICKAXE, level);
        axe = new OmniToolType(Tools.AXE, level);
        shovel = new OmniToolType(Tools.SHOVEL, level);
    }

    public @NotNull OmniToolType getPickaxe() {
        return pickaxe;
    }

    public @NotNull OmniToolType getAxe() {
        return axe;
    }

    public @NotNull OmniToolType getShovel() {
        return shovel;
    }

}
