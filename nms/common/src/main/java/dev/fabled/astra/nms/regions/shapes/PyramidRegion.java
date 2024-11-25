package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.AbstractRegion;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class PyramidRegion implements AbstractRegion {

    private final int startX, startZ, endX, endZ, y;
    private final @NotNull List<int[]> locations;

    public PyramidRegion(
            final int startX,
            final int startZ,
            final int endX,
            final int endZ,
            final int y
    ) {
        this.startX = startX;
        this.startZ = startZ;
        this.endX = endX;
        this.endZ = endZ;
        this.y = y;
        locations = calculateLocations();
    }

    @Override
    public @NotNull List<int[]> calculateLocations() {
        final List<int[]> locations = new ArrayList<>();

        int y = y();
        int minX = minX();
        int maxX = maxX();
        int minZ = minZ();
        int maxZ = maxZ();

        while (minX != maxX || minZ != maxZ) {
            for (int i = minX; i <= maxX; i++) {
                for (int j = minZ; j <= maxZ; j++) {
                    locations.add(new int[]{i, y, j});
                }
            }

            if (minX != maxX) {
                minX++;
                maxX--;
            }

            if (minZ != maxZ) {
                minZ++;
                maxZ--;
            }

            y++;
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

    public int minZ() {
        return Math.min(startZ, endZ);
    }

    public int maxZ() {
        return Math.max(startZ, endZ);
    }

    public int startX() {
        return startX;
    }

    public int startZ() {
        return startZ;
    }

    public int endX() {
        return endX;
    }

    public int endZ() {
        return endZ;
    }

    public int y() {
        return y;
    }

}
