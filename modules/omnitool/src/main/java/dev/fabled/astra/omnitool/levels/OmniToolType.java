package dev.fabled.astra.omnitool.levels;

import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.omnitool.Tools;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public final class OmniToolType {

    private @NotNull Material material;
    private final int customModelData;
    private final @NotNull String displayName;
    private final @NotNull String levelColor;

    public OmniToolType(final @NotNull Tools tool, final int level) {
        final YamlConfig config = OmniToolModule.getInstance().getLevelsYml();
        final String key = level > 0
                ? "levels." + level + "." + tool.name().toLowerCase() + "."
                : "level-0." + tool.name().toLowerCase() + ".";

        final String material = config.options().getString(key + "material", "");
        try { this.material = Material.valueOf(material); }
        catch (IllegalArgumentException e) {
            this.material = tool.getDef();
        }

        customModelData = config.options().getInt(key + "custom-model-data", 0);

        displayName = config.options().getString(
                key + "display-name",
                "<b><gray>Omni-Tool</b> <dark_gray>(<gray>" + tool.name().toLowerCase() + "<dark_gray>)"
        );

        levelColor = config.options().getString(key + "level-color", "<gray>");
    }

    public @NotNull Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public @NotNull String getDisplayName() {
        return displayName;
    }

    public @NotNull String getLevelColor() {
        return levelColor;
    }

}
