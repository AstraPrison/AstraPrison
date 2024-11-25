package dev.fabled.astra.modules.impl;

import dev.fabled.astra.modules.AstraModule;
import org.jetbrains.annotations.NotNull;

public class MinesModule extends AstraModule {

    public static final @NotNull String ID;

    static {
        ID = "mines";
    }

    private static MinesModule instance;

    public static MinesModule getInstance() {
        if (instance == null) {
            instance = new MinesModule();
        }

        return instance;
    }

    private MinesModule() {
        super(ID);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {

    }

}
