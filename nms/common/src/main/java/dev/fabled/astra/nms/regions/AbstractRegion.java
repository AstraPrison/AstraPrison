package dev.fabled.astra.nms.regions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AbstractRegion {

    @NotNull List<int[]> calculateLocations();
    @NotNull List<int[]> getLocations();

}
