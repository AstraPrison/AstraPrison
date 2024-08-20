package dev.fabled.astra.api.actions;

import dev.fabled.astra.AstraPrisonAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AstraAction {

    private final @NotNull String id;

    public AstraAction(final @NotNull String id) {
        this.id = id;
    }

    public @NotNull String getID() {
        return id;
    }

    public void register() {
        final AstraPrisonAPI api = AstraPrisonAPI.get();
        if (api == null) {
            Bukkit.getConsoleSender().sendMessage("[Astra] Could not register action " + id + " because the plugin is not loaded yet!");
            return;
        }

        final ActionManager actionManager = api.getActionManager();
        if (actionManager == null) {
            Bukkit.getConsoleSender().sendMessage("[Astra] Could not register action " + id + " because the action manager is not loaded yet!");
            return;
        }

        actionManager.register(this);
    }

    public abstract void run(final @NotNull Player player, @NotNull ActionArgument argument);

}
