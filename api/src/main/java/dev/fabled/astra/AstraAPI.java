package dev.fabled.astra;

import dev.fabled.astra.api.actions.ClickActionManager;
import dev.fabled.astra.api.requirements.ClickRequirementManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public interface AstraAPI {

    /**
     * Get AstraPrison's instance of the API
     * @return AstraPrison's instance of the {@link AstraAPI} or <code>null</code> if AstraPrison is not on the server
     */
    static @Nullable AstraAPI getAPI() {
        final Plugin plugin = Bukkit.getPluginManager().getPlugin("Astra");
        if (plugin instanceof AstraAPI api) {
            return api;
        }

        return null;
    }

    /**
     * Get AstraPrison's instance of the {@link ClickActionManager}
     * @return AstraPrison's instance of the {@link ClickActionManager} or <code>null</code> if AstraPrison is not loaded yet
     */
    @Nullable
    ClickActionManager getClickActionManager();

    /**
     * Get AstraPrison's instance of the {@link ClickRequirementManager}
     * @return AstraPrison's instance of the {@link ClickRequirementManager} or <code>null</code> if AstraPrison is not loaded yet
     */
    @Nullable
    ClickRequirementManager getClickRequirementManager();

}
