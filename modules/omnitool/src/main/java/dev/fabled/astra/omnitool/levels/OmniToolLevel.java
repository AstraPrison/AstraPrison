package dev.fabled.astra.omnitool.levels;

import dev.fabled.astra.omnitool.ToolType;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

public final class OmniToolLevel {

    private final @NotNull OmniToolType pickaxe;
    private final @NotNull OmniToolType axe;
    private final @NotNull OmniToolType shovel;

    public OmniToolLevel(final @NotNull YamlConfig config, final int level) {
        pickaxe = new OmniToolType(config, ToolType.PICKAXE, level);
        axe = new OmniToolType(config, ToolType.AXE, level);
        shovel = new OmniToolType(config, ToolType.SHOVEL, level);
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
