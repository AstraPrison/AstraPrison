package dev.fabled.astra.utils.tools;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum ToolType {

    PICKAXE(Tag.MINEABLE_PICKAXE),
    AXE(Tag.MINEABLE_AXE),
    SHOVEL(Tag.MINEABLE_SHOVEL);

    private final Tag<Material> tag;

    ToolType(final @NotNull Tag<Material> tag) {
        this.tag = tag;
    }

    public boolean isToolType(final @NotNull ItemStack itemStack) {
        return itemStack.getType().name().endsWith(this.name());
    }

    public boolean isPreferredTool(final @NotNull BlockState blockState) {
        return tag.isTagged(blockState.getType());
    }

    public static @NotNull ToolType getToolType(final @NotNull ItemStack itemStack) {
        for (final ToolType type : ToolType.values()) {
            if (type.isToolType(itemStack)) {
                return type;
            }
        }

        return ToolType.PICKAXE;
    }

    public static @NotNull ToolType getPreferredToolType(final @NotNull BlockState blockState) {
        for (final ToolType type : ToolType.values()) {
            if (type.tag.isTagged(blockState.getType())) {
                return type;
            }
        }

        return ToolType.PICKAXE;
    }

}
