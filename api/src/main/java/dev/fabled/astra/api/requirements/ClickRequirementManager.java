package dev.fabled.astra.api.requirements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ClickRequirementManager {

    /**
     * Register an {@link AbstractItemClickRequirement} to AstraPrison's {@link ClickRequirementManager} instance
     * @param requirement {@link AbstractItemClickRequirement}
     */
    void register(final @NotNull AbstractItemClickRequirement requirement);

    /**
     * Check if an {@link AbstractItemClickRequirement} exists by its ID
     * @param id {@link String} the requirement's ID
     * @return {@link Boolean} <code>true</code> if it's registered, <code>false</code> if not
     */
    boolean exists(final @NotNull String id);

    /**
     * Get an {@link AbstractItemClickRequirement} from its ID
     * @param id {@link String} the requirement's ID
     * @return {@link AbstractItemClickRequirement} or <code>null</code> if this ID is not registered
     * @see ClickRequirementManager#exists(String)
     */
    @Nullable AbstractItemClickRequirement getRequirement(final @NotNull String id);

    /**
     * Get a list of all registered {@link AbstractItemClickRequirement} IDs
     * @return {@link List}<{@link String}>
     */
    @NotNull List<String> getRequirementIDs();

}
