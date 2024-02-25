package dev.paracausal.astra.api.actions;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public abstract class Action {

    @NotNull final String id;

    public Action(@NotNull final String id) {
        this.id = id;
    }

    public @NotNull String getID() {
        return id;
    }

    public abstract void run(@NotNull final OfflinePlayer player, @NotNull ActionArgument argument);

}
