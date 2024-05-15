package dev.fabled.astra.modules;

import dev.fabled.astra.Astra;
import org.jetbrains.annotations.NotNull;

public class AstraModule {

    final @NotNull String id;

    public AstraModule(@NotNull final String id) {
        this.id = id;
        Astra.getUtilities().getModuleManager().register(this);
    }

    public @NotNull String getID() {
        return id;
    }

    public void onLoad() {}
    public void onEnable() {}
    public void onReload() {}
    public void onDisable() {}

}
