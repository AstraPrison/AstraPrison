package dev.fabled.astra;

import dev.fabled.astra.api.actions.ActionManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public interface AstraPrisonAPI {

    static @Nullable AstraPrisonAPI get() {
        return (AstraPrisonAPI) Bukkit.getPluginManager().getPlugin("AstraPrison");
    }

    /**
     * Get Astra's instance of the ActionManager
     *
     * @return {@link ActionManager} or null if the plugin has not been loaded yet
     */
    @Nullable ActionManager getActionManager();

}
