package dev.fabled.astra.api.requirements;

import dev.fabled.astra.AstraAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractItemClickRequirement {

    private final @NotNull String id;

    public AbstractItemClickRequirement(final @NotNull String id) {
        this.id = id;
    }

    /**
     * Gets the ID for this {@link AbstractItemClickRequirement}
     * @return {@link String}
     */
    public final @NotNull String getId() {
        return id;
    }

    /**
     * Register your {@link AbstractItemClickRequirement} to AstraPrison's {@link ClickRequirementManager} instance
     */
    public final void register() {
        final AstraAPI api = AstraAPI.getAPI();
        if (api == null) {
            return;
        }

        final ClickRequirementManager manager = api.getClickRequirementManager();
        if (manager != null) {
            manager.register(this);
        }
    }

    /**
     * Run the {@link AbstractItemClickRequirement} checks for the specific player with the specific options from config
     * @param player {@link Player}
     * @param options {@link ClickRequirementOptions}
     * @return {@link Boolean} <code>true</code> if they meet the requirements, <code>false</code> if not
     */
    public abstract @NotNull Boolean check(final @NotNull Player player, final @NotNull ClickRequirementOptions options);

}
