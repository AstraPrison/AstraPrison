package dev.fabled.astra.utils.blocks;

@SuppressWarnings("UnstableApiUsage")
public class BlockPosition implements io.papermc.paper.math.BlockPosition {

    private final int x, y, z;

    public BlockPosition(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

}
