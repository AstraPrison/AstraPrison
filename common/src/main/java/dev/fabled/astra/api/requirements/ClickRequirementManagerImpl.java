package dev.fabled.astra.api.requirements;

import dev.fabled.astra.api.requirements.impl.IsOfflineClickRequirement;
import dev.fabled.astra.api.requirements.impl.IsOnlineClickRequirement;
import dev.fabled.astra.api.requirements.impl.PermissionClickRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ClickRequirementManagerImpl implements ClickRequirementManager {

    private static ClickRequirementManagerImpl instance;
    private static final @NotNull Set<AbstractItemClickRequirement> DEFAULT_CLICK_REQUIREMENTS;

    static {
        DEFAULT_CLICK_REQUIREMENTS = Set.of(
                new PermissionClickRequirement(),
                new IsOnlineClickRequirement(),
                new IsOfflineClickRequirement()
        );
    }

    public static ClickRequirementManagerImpl getInstance() {
        if (instance == null) {
            instance = new ClickRequirementManagerImpl();
        }

        return instance;
    }

    private final @NotNull Map<String, AbstractItemClickRequirement> requirements;

    private ClickRequirementManagerImpl() {
        requirements = new HashMap<>();
        DEFAULT_CLICK_REQUIREMENTS.forEach(this::register);
    }

    @Override
    public void register(final @NotNull AbstractItemClickRequirement requirement) {
        requirements.putIfAbsent(requirement.getId(), requirement);
    }

    @Override
    public boolean exists(final @NotNull String id) {
        return requirements.containsKey(id);
    }

    @Override
    public @Nullable AbstractItemClickRequirement getRequirement(final @NotNull String id) {
        return requirements.getOrDefault(id, null);
    }

    @Override
    public @NotNull List<String> getRequirementIDs() {
        return new ArrayList<>(requirements.keySet());
    }

}
