package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.AbstractRegion;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CylinderRegion implements AbstractRegion {

    private final int x, z, radius, startY, endY;
    private final @NotNull List<int[]> locations;

    public CylinderRegion(
            final int x,
            final int z,
            final int radius,
            final int startY,
            final int endY
    ) {
        this.x = x;
        this.z = z;
        this.radius = radius;
        this.startY = startY;
        this.endY = endY;
        locations = calculateLocations();
    }

    @Override
    public @NotNull List<int[]> calculateLocations() {
        final List<int[]> locations = new ArrayList<>();
        final int diameter = radius * 2;

        for (int y = minY(); y <= maxY(); y++) {
            for (int i = x; i < x + diameter; i++) {
                for (int j = z; j < z + diameter; j++) {
                    locations.add(new int[]{i, y, j});
                }
            }
        }

        return locations;
    }

    @Override
    public @NotNull List<int[]> getLocations() {
        return new ArrayList<>(locations);
    }

    public int minY() {
        return Math.min(startY, endY);
    }

    public int maxY() {
        return Math.max(startY, endY);
    }

    public int x() {
        return x;
    }

    public int z() {
        return z;
    }

    public int radius() {
        return radius;
    }

    public int startY() {
        return startY;
    }

    public int endY() {
        return endY;
    }

}
