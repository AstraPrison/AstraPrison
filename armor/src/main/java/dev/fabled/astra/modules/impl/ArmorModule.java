package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.commands.ArmorCommand;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.modules.AstraModule;
import org.jetbrains.annotations.NotNull;

public class ArmorModule extends AstraModule {

    public static final String ID = "armor";
    private static ArmorModule instance;

    public static @NotNull ArmorModule getInstance() {
        if (instance == null) {
            final AstraModule module = Astra.getUtilities().getModuleManager().getModule(ID);
            instance = module == null
                    ? new ArmorModule()
                    : (ArmorModule) module;
        }

        return instance;
    }

    public ArmorModule() {
        super(ID);
    }

    @Override
    public void onEnable() {
        final CommandManager commandManager = Astra.getUtilities().getCommandManager();
        commandManager.register(new ArmorCommand());
    }

}
