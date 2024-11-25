package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.AbstractRegion;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CuboidRegion implements AbstractRegion {

    private final int startX, startY, startZ, endX, endY, endZ;
    private final @NotNull List<int[]> locations;

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
        locations = calculateLocations();
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

    @Override
    public @NotNull List<int[]> calculateLocations() {
        final List<int[]> locations = new ArrayList<>();

        for (int x = minX(); x <= maxX(); x++) {
            for (int y = minY(); y <= maxY(); y++) {
                for (int z = minZ(); z <= maxZ(); z++) {
                    locations.add(new int[]{x, y, z});
                }
            }
        }

        return locations;
    }

    @Override
    public @NotNull List<int[]> getLocations() {
        return new ArrayList<>(locations);
    }

    public int minX() {
        return Math.min(startX, endX);
    }

    public int maxX() {
        return Math.max(startX, endX);
    }

    public int minY() {
        return Math.min(startY, endY);
    }

    public int maxY() {
        return Math.max(startY, endY);
    }

    public int minZ() {
        return Math.min(startZ, endZ);
    }

    public int maxZ() {
        return Math.max(startZ, endZ);
    }

    public int startX() {
        return startX;
    }

    public int startY() {
        return startY;
    }

    public int startZ() {
        return startZ;
    }

    public int endX() {
        return endX;
    }

    public int endY() {
        return endY;
    }

    public int endZ() {
        return endZ;
    }

}
