package dev.paracausal.astra.api.actions;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ActionManager {

    /**
     * Register an action to the plugin
     * @param action Action
     */
    void register(@NotNull final Action action);

    /**
     * Check if an action is registered
     * @param id Action ID
     * @return boolean
     */
    boolean exists(@NotNull final String id);

    /**
     * Get the default action
     * @return Action
     */
    @Nullable Action getDefaultAction();

    /**
     * Get an action by its ID
     * @param id Action ID
     * @return Action
     */
    @Nullable Action getAction(@NotNull final String id);

    /**
     * Get an action from a commandLine
     * @param commandLine A string formatted for actions!<br>Example: '[actionID] optional strings that make up a command line!'
     * @return Action
     */
    @Nullable Action getFromCommandLine(@NotNull final String commandLine);

    /**
     * Removes the action ID from the commandLine if present
     * @param commandLine A string formatted for actions!<br>Example: '[actionID] optional strings that make up a command line!'
     * @return commandLine without the [actionID] at the start!
     */
    @NotNull String replaceCommandLine(@NotNull String commandLine);

    void runCommandLine(@NotNull final OfflinePlayer player, @NotNull String commandLine);
    void runCommandLine(@NotNull final OfflinePlayer player, @NotNull final List<String> commandLines);

}
