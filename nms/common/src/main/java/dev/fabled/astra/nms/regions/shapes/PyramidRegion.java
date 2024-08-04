package dev.fabled.astra.nms.regions.shapes;

import dev.fabled.astra.nms.regions.ShapedRegion;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record PyramidRegion(
        int startX,
        int startZ,
        int endX,
        int endZ,
        int y
) implements ShapedRegion {

    public PyramidRegion(
            final int startX,
            final int startZ,
            final int endX,
            final int endZ,
            int y
    ) {
        this.startX = startX;
        this.startZ = startZ;
        this.endX = endX;
        this.endZ = endZ;
        this.y = y;
    }

    public PyramidRegion(final @NotNull Location cornerOne, final @NotNull Location cornerTwo) {
        this(
                cornerOne.getBlockX(),
                cornerOne.getBlockZ(),
                cornerTwo.getBlockX(),
                cornerTwo.getBlockZ(),
                Math.min(cornerOne.getBlockY(), cornerTwo.getBlockY())
        );
    }

    int minX() {
        return Math.min(startX, endX);
    }

    int minZ() {
        return Math.min(startZ, endZ);
    }

    int maxX() {
        return Math.max(startX, endX);
    }

    int maxZ() {
        return Math.max(startZ, endZ);
    }

    @Override
    public @NotNull List<int[]> getLocations() {
        final List<int[]> result = new ArrayList<>();

        int y = y();
        int minX = minX();
        int maxX = maxX();
        int minZ = minZ();
        int maxZ = maxZ();

        while (minX != maxX || minZ != maxZ) {
            for (int i = minX; i <= maxX; i++) {
                for (int k = minZ; k <= maxZ; k++) {
                    result.add(new int[]{i, y, k});
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

        return result;
    }

}
