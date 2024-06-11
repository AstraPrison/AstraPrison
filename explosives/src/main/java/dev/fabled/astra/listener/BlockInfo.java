package dev.fabled.astra.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class BlockInfo {
    private final Material blockType;
    private final Location location;
    private Material type;
    private final BlockData blockData;

    public BlockInfo(Location location, Material blockType, BlockData blockData) {
        this.location = location;
        this.type = type;
        this.blockData = blockData;
        this.blockType = blockType;
    }

    public Location getLocation() {
        return location;
    }

    public Material getType() {
        return type;
    }

    public Material getBlockType() {
        return blockType;
    }

    public BlockData getBlockData() {
        return blockData;
    }


}
