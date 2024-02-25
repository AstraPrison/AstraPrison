package dev.paracausal.astra.api.requirements;

import dev.paracausal.astra.api.requirements.impl.StringEqualsRequirement;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RequirementManagerImpl implements RequirementManager {

    private final Map<String, Requirement> requirements;

    public RequirementManagerImpl() {
        requirements = new HashMap<>();
    }

    public void onLoad() {
        register(new StringEqualsRequirement());
    }

    @Override
    public void register(@NotNull final Requirement requirement) {
        for (final String id : requirement.getIDs()) {
            requirements.putIfAbsent(id, requirement);
        }
    }

}
