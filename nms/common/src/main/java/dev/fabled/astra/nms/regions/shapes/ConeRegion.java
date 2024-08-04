package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.ShapedRegion;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record ConeRegion(
        int x,
        int y,
        int z,
        int radius
) implements ShapedRegion {

    public ConeRegion(
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

    public ConeRegion(final @NotNull Location location, final int radius) {
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
        final int diameter = radius * 2;

        for (int i = x; i < x + diameter; i++) {
            for (int j = y; j < y + diameter; j++) {
                for (int k = z; k < z + diameter; k++) {
                    if (Math.pow(i, 2) + Math.pow(k, 2) == Math.pow(j, 2)) {
                        result.add(new int[]{i, j, k});
                    }
                }
            }
        }

        return result;
    }

}
