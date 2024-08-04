package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.ShapedRegion;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record CuboidRegion(
        int startX,
        int startY,
        int startZ,
        int endX,
        int endY,
        int endZ
) implements ShapedRegion {

    public CuboidRegion(
            final int startX,
            final int startY,
            final int startZ,
            final int endX,
            final int endY,
            final int endZ
    ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
    }

    public CuboidRegion(final @NotNull Location cornerOne, final @NotNull Location cornerTwo) {
        this(
                cornerOne.getBlockX(),
                cornerOne.getBlockY(),
                cornerOne.getBlockZ(),
                cornerTwo.getBlockX(),
                cornerTwo.getBlockY(),
                cornerTwo.getBlockZ()
        );
    }

    public int minX() {
        return Math.min(startX, endX);
    }

    public int minY() {
        return Math.min(startY, endY);
    }

    public int minZ() {
        return Math.min(startZ, endZ);
    }

    public int maxX() {
        return Math.max(startX, endX);
    }

    public int maxY() {
        return Math.max(startY, endY);
    }

    public int maxZ() {
        return Math.max(startZ, endZ);
    }

    @Override
    public @NotNull List<int[]> getLocations() {
        final List<int[]> result = new ArrayList<>();

        for (int x = minX(); x <= maxX(); x++) {
            for (int y = minY(); y <= maxY(); y++) {
                for (int z = minZ(); z <= maxZ(); z++) {
                    result.add(new int[]{x, y, z});
                }
            }
        }

        return result;
    }

}
