package dev.fabled.astra.utils.blocks;

import io.papermc.paper.math.FinePosition;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class BlockFinePosition implements FinePosition {
    
    private final double x, y, z;
    
    public BlockFinePosition(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockFinePosition(final @NotNull Location location) {
        this(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }
}
