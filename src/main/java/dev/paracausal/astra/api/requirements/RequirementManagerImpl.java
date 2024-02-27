package dev.paracausal.astra.api.requirements;

import dev.paracausal.astra.api.requirements.impl.HasAnyPermissionRequirement;
import dev.paracausal.astra.api.requirements.impl.HasPermissionRequirement;
import dev.paracausal.astra.api.requirements.impl.StringEqualsAnyRequirement;
import dev.paracausal.astra.api.requirements.impl.StringEqualsRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RequirementManagerImpl implements RequirementManager {

    private final Map<String, Requirement> requirements;

    public RequirementManagerImpl() {
        requirements = new HashMap<>();
    }

    public void onLoad() {
        register(new StringEqualsRequirement());
        register(new StringEqualsAnyRequirement());
        register(new HasPermissionRequirement());
        register(new HasAnyPermissionRequirement());
    }

    @Override
    public void register(@NotNull final Requirement requirement) {
        for (final String id : requirement.getIDs()) {
            requirements.putIfAbsent(id.toLowerCase(), requirement);
        }
    }

    @Override
    public boolean exists(@NotNull String id) {
        return requirements.containsKey(id.toLowerCase());
    }

    @Override
    public @Nullable Requirement getRequirement(@NotNull String id) {
        return requirements.getOrDefault(id.toLowerCase(), null);
    }

}
