package dev.fabled.astra.mines.wand;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("ALL")
public class BlockPosition implements io.papermc.paper.math.BlockPosition {

    private final UUID world;
    private final int x, y, z;

    public BlockPosition(@NotNull final Location location) {
        world = location.getWorld().getUID();
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
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

    public boolean isSameWorld(@NotNull final World world) {
        return world.getUID().compareTo(this.world) == 0;
    }

}
