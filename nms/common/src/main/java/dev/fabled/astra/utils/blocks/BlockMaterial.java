package dev.fabled.astra.utils.blocks;

import dev.fabled.astra.exceptions.InvalidBlockMaterialException;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BlockMaterial {

    private final Material material;

    public BlockMaterial(final @NotNull Material material) throws InvalidBlockMaterialException {
        if (material.isAir() || !material.isBlock()) {
            throw new InvalidBlockMaterialException(material + " is not a block!");
        }

        this.material = material;
    }

    public @NotNull Material getMaterial() {
        return material;
    }

}
