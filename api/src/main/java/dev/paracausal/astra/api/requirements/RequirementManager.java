package dev.paracausal.astra.api.requirements;

import org.jetbrains.annotations.NotNull;

public interface RequirementManager {

    void register(@NotNull final Requirement requirement);

}
