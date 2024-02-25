package dev.paracausal.astra.api.requirements;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Requirement {

    private @NotNull final String[] ids;

    public Requirement(@NotNull final String... ids) {
        this.ids = ids;
    }

    public @NotNull String[] getIDs() {
        return ids;
    }

    public abstract boolean check(@NotNull final Player player, RequirementOptions options);

}
