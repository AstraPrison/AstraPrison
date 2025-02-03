package dev.fabled.astra.api.actions;

import org.jetbrains.annotations.NotNull;

public final class ItemClickActionArgs {

    private final @NotNull String commandLine;
    private final @NotNull String[] splitCommandLine;

    public ItemClickActionArgs(final @NotNull String commandLine) {
        this.commandLine = commandLine;
        this.splitCommandLine = commandLine.split(" ");
    }

    public @NotNull String getCommandLine() {
        return commandLine;
    }

    public @NotNull String[] getSplitCommandLine() {
        return splitCommandLine;
    }

}
