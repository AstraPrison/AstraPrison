package dev.fabled.astra.modules;

import org.jetbrains.annotations.NotNull;

public abstract class AstraModule {

    private final @NotNull String id;

    public AstraModule(final @NotNull String id) {
        this.id = id;
    }

    public final @NotNull String getId() {
        return id;
    }

    /**
     * Called from the main class' onLoad method
     */
    public abstract void onLoad();

    /**
     * Called from the main class' onEnable method
     */
    public abstract void onEnable();

    /**
     * Called from the main class' onDisable method
     */
    public abstract void onDisable();

    /**
     * Called from the main class' onReload method
     */
    public abstract void onReload();

}
