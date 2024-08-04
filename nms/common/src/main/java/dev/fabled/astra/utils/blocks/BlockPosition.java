package dev.fabled.astra.utils.blocks;

import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class BlockPosition implements Position {

    private final int x, y, z;

    public BlockPosition(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPosition(final @NotNull Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public int blockX() {
        return x;
    }

    @Override
    public int blockY() {
        return y;
    }

    @Override
    public int blockZ() {
        return z;
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

    @Override
    public boolean isBlock() {
        return true;
    }

    @Override
    public boolean isFine() {
        return false;
    }

    @Override
    public @NotNull Position offset(int x, int y, int z) {
        return new BlockPosition(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public @NotNull FinePosition offset(double x, double y, double z) {
        return new BlockFinePosition(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public io.papermc.paper.math.@NotNull BlockPosition toBlock() {
        return null;
    }

}
