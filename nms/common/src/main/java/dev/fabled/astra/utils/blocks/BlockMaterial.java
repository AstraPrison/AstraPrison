package dev.fabled.astra.utils.blocks;

import dev.fabled.astra.exceptions.InvalidBlockMaterialException;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public final class BlockMaterial {

    private final Material material;

    public BlockMaterial(final @NotNull Material material) throws InvalidBlockMaterialException {
        if (material.isAir() || !material.isBlock()) {
            throw new InvalidBlockMaterialException(material + " is not a block!");
        }

        this.material = material;
    }

    public BlockMaterial(final @NotNull Block block) {
        material = block.getType();
    }

    public @NotNull Material getMaterial() {
        return material;
    }

}
