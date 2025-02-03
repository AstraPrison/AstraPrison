package dev.fabled.astra.api.actions;

import dev.fabled.astra.AstraAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractItemClickAction {

    private final @NotNull String id;

    public AbstractItemClickAction(final @NotNull String id) {
        this.id = id;
    }

    /**
     * Gets the ID for this {@link AbstractItemClickAction}
     * @return {@link String}
     */
    public final @NotNull String getId() {
        return id;
    }

    /**
     * Register your {@link AbstractItemClickAction} to AstraPrison's {@link ClickActionManager} instance
     */
    public final void register() {
        final AstraAPI api = AstraAPI.getAPI();
        if (api == null) {
            return;
        }

        final ClickActionManager manager = api.getClickActionManager();
        if (manager != null) {
            manager.register(this);
        }
    }

    /**
     * Run the {@link AbstractItemClickAction} for the specific player with the specified arguments
     * @param player {@link Player}
     * @param arguments {@link ItemClickActionArgs}
     */
    public abstract void run(final @NotNull Player player, final @NotNull ItemClickActionArgs arguments);

}
