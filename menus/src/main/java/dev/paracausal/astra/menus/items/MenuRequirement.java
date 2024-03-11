package dev.paracausal.astra.menus.items;

import dev.paracausal.astra.api.requirements.Requirement;
import dev.paracausal.astra.api.requirements.RequirementOptions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record MenuRequirement(
        @NotNull String id,
        @NotNull Requirement requirement,
        @NotNull RequirementOptions options,
        @NotNull List<String> commandLines
) {

    public MenuRequirement(
            @NotNull final String id,
            @NotNull final Requirement requirement,
            @NotNull final RequirementOptions options,
            @NotNull final List<String> commandLines
    ) {
        this.id = id;
        this.requirement = requirement;
        this.options = options;
        this.commandLines = new ArrayList<>(commandLines);
    }

}
