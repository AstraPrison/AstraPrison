package dev.fabled.astra.api.actions;

import dev.fabled.astra.utils.ActionUtils;
import org.jetbrains.annotations.NotNull;

public class ActionArgument {

    private final @NotNull String actionId;
    private final @NotNull String commandLine;
    private final @NotNull String[] arguments;

    public ActionArgument(@NotNull String commandLine) {
        actionId = ActionUtils.getIdFromCommandLine(commandLine);

        if (commandLine.startsWith("[" + actionId + "]")) {
            commandLine = commandLine.substring(actionId.length() + 2);
            if (commandLine.startsWith(" ")) {
                commandLine = commandLine.substring(1);
            }
        }

        this.commandLine = commandLine;
        this.arguments = commandLine.split(" ");
    }

    public ActionArgument(final @NotNull String commandLine, final @NotNull String actionId) {
        this.actionId = actionId;
        this.commandLine = commandLine;
        this.arguments = commandLine.split(" ");
    }

    public @NotNull String getActionId() {
        return actionId;
    }

    public @NotNull String getCommandLine() {
        return commandLine;
    }

    public @NotNull String[] getArguments() {
        return arguments;
    }

}
