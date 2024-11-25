package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.AbstractRegion;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SphereRegion implements AbstractRegion {

    private final int x, y, z, radius;
    private final @NotNull List<int[]> locations;

    public SphereRegion(
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

    public SphereRegion(final @NotNull Location location, final int radius) {
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

        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                for (int k = z - radius; k <= z + radius; k++) {
                    double distance = Math.pow(x - i, 2) + Math.pow(y - j, 2) + Math.pow(z - k, 2);
                    if (distance < Math.pow(radius, 2) && distance < Math.pow(radius - 1, 2)) {
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
