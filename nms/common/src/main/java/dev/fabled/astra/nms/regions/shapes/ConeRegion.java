package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.AbstractRegion;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ConeRegion implements AbstractRegion {

    private final int x, y, z, radius;
    private final @NotNull List<int[]> locations;

    public ConeRegion(
            int x,
            int y,
            int z,
            int radius
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        locations = calculateLocations();
    }

    public ConeRegion(final @NotNull Location location, final int radius) {
        this(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                radius
        );
    }

    @Override
    public @NotNull List<int[]> calculateLocations() {
        final List<int[]> locations = new ArrayList<>();
        final int diameter = radius * 2;

        for (int i = x; i < x + diameter; i++) {
            for (int j = y; j < y + diameter; j++) {
                for (int k = z; k < z + diameter; k++) {
                    if (Math.pow(i, 2) + Math.pow(k, 2) == Math.pow(j, 2)) {
                        locations.add(new int[]{i, j, k});
                    }
                }
            }
        }

        return locations;
    }

    @Override
    public @NotNull List<int[]> getLocations() {
        return new ArrayList<>(locations);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public int radius() {
        return radius;
    }

}
