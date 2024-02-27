package dev.paracausal.astra.api.requirements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RequirementManager {

    void register(@NotNull final Requirement requirement);
    boolean exists(@NotNull final String id);
    @Nullable Requirement getRequirement(@NotNull final String id);

}
