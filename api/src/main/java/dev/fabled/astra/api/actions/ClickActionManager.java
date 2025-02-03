package dev.fabled.astra.api.actions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ClickActionManager {

    /**
     * Register an {@link AbstractItemClickAction} to AstraPrison's {@link ClickActionManager} instance
     * @param action {@link AbstractItemClickAction}
     */
    void register(final @NotNull AbstractItemClickAction action);

    /**
     * Check if an {@link AbstractItemClickAction} exists by its ID
     * @param id {@link String} the action's ID
     * @return {@link Boolean} <code>true</code> if it's registered, <code>false</code> if not
     */
    @NotNull Boolean exists(final @NotNull String id);

    /**
     * Get an {@link AbstractItemClickAction} from its ID
     * @param id {@link String} the action's ID
     * @return {@link AbstractItemClickAction} or <code>null</code> if this ID is not registered
     * @see ClickActionManager#exists(String) 
     */
    @Nullable AbstractItemClickAction getAction(final @NotNull String id);

    /**
     * Return an {@link AbstractItemClickAction} from its ID in the full command line
     * @param fullCommandLine {@link String} The full command line string<br>Example: <code>"[message] Hello there!"</code>
     * @return The {@link AbstractItemClickAction} from its ID, or the default action
     */
    @NotNull AbstractItemClickAction getActionFromString(final @NotNull String fullCommandLine);

    /**
     * Get a list of all registered {@link AbstractItemClickAction} IDs
     * @return {@link List}<{@link String}>
     */
    @NotNull List<String> getActionIDs();

    @NotNull String removeActionIdFromString(final @NotNull String fullCommandLine);

    static @NotNull String removeActionIdFromString(final @NotNull String actionId, final @NotNull String fullCommandLine) {
        if (!fullCommandLine.startsWith("[")) {
            return fullCommandLine;
        }

        String commandLine = fullCommandLine.substring(actionId.length() + 2);
        if (commandLine.startsWith(" ")) {
            commandLine = commandLine.substring(1);
        }

        return commandLine;
    }

}
