package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.ShapedRegion;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record CylinderRegion(
        int x,
        int z,
        int radius,
        int startY,
        int endY
) implements ShapedRegion {

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
    }

    public CylinderRegion(final @NotNull Location location, final int radius, final int height) {
        this(
                location.getBlockX(),
                location.getBlockZ(),
                radius,
                location.getBlockY(),
                location.getBlockY() + height
        );
    }

    public int minY() {
        return Math.min(startY, endY);
    }

    public int maxY() {
        return Math.max(startY, endY);
    }

    @Override
    public @NotNull List<int[]> getLocations() {
        final List<int[]> result = new ArrayList<>();
        final int diameter = radius * 2;

        for (int y = minY(); y <= maxY(); y++) {
            for (int i = x; i < x + diameter; i++) {
                for (int k = z; k < z + diameter; k++) {
                    result.add(new int[]{i, y, k});
                }
            }
        }

        return result;
    }

}
