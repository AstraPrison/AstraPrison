package dev.fabled.astra.mines.generator;

import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerBlockChanges {

    private @NotNull final Collection<BlockState> blockStates;

    public PlayerBlockChanges() {
        blockStates = new ArrayList<>();
    }

    public void addBlockState(@NotNull final BlockState... blockState) {
        blockStates.addAll(Arrays.asList(blockState));
    }

    public void addBlockState(@NotNull final List<BlockState> blockState) {
        blockStates.addAll(blockState);
    }

    public @NotNull Collection<BlockState> getBlockStates() {
        return new ArrayList<>(blockStates);
    }

}
