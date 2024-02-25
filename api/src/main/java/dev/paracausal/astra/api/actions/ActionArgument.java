package dev.paracausal.astra.api.actions;

import org.jetbrains.annotations.NotNull;

public final class ActionArgument {

    private @NotNull final String commandLine;
    private @NotNull final String[] arguments;

    public ActionArgument(@NotNull final String commandLine) {
        this.commandLine = commandLine;
        this.arguments = commandLine.split(" ");
    }

    public @NotNull String getCommandLine() {
        return commandLine;
    }

    public @NotNull String[] getArguments() {
        return arguments;
    }

}
