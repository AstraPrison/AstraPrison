package dev.paracausal.astra.menus.items;

import dev.paracausal.astra.api.requirements.Requirement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record MenuRequirement(
        @NotNull String id,
        @NotNull Requirement requirement,
        @NotNull List<String> commandLines
) {

    public MenuRequirement(
            @NotNull final String id,
            @NotNull final Requirement requirement,
            @NotNull final List<String> commandLines
    ) {
        this.id = id;
        this.requirement = requirement;
        this.commandLines = new ArrayList<>(commandLines);
    }

}
