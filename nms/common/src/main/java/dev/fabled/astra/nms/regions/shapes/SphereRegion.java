package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.ShapedRegion;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record SphereRegion(
        int x,
        int y,
        int z,
        int radius
) implements ShapedRegion {

    public SphereRegion(
            final int x,
            final int y,
            final int z,
            final int radius
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
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
    public @NotNull List<int[]> getLocations() {
        final List<int[]> result = new ArrayList<>();

        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                for (int k = z - radius; k <= z + radius; k++) {

                    double distance = (Math.pow(x - i, 2) + Math.pow(y - j, 2) + Math.pow(z - k, 2));
                    if (distance < Math.pow(radius, 2) && distance < Math.pow(radius - 1, 2)) {
                        result.add(new int[]{i, j, k});
                    }

                }
            }
        }

        return result;
    }

}
