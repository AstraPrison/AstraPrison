package dev.fabled.astra.api.actions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ActionManager {

    @NotNull String getDefaultActionId();

    /**
     * Register an action
     * @param action {@link AstraAction}
     */
    void register(final @NotNull AstraAction action);

    /**
     * Check if an action exists by its ID
     * @param id {@link String}
     * @return {@link Boolean}
     */
    boolean exists(final @NotNull String id);

    /**
     * Get an action by its ID
     * @param id {@link String}
     * @return {@link AstraAction}
     */
    @Nullable AstraAction getAction(final @NotNull String id);

    /**
     * Get a list of registered action IDs
     * @return {@link List}
     */
    @NotNull List<String> getActionIDs();

}
