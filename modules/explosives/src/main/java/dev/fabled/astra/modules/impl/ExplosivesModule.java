package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.ExplosivesCommand;
import dev.fabled.astra.commands.RpgCommand;
import dev.fabled.astra.modules.AstraModule;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

public class ExplosivesModule extends AstraModule {

    public static final String ID = "explosives";
    private static ExplosivesModule instance;

    public static @NotNull ExplosivesModule getInstance() {
        if (instance == null) {
            final AstraModule module = Astra.getUtilities().getModuleManager().getModule(ID);
            instance = module == null
                    ? new ExplosivesModule()
                    : (ExplosivesModule) module;
        }

        return instance;
    }

    private YamlConfig rpgYml;

    public ExplosivesModule() {
        super(ID);
    }

    @Override
    public void onLoad() {
        rpgYml = new YamlConfig("modules/explosives/rpg");
    }

    @Override
    public void onEnable() {
        final CommandManager commandManager = Astra.getUtilities().getCommandManager();
        commandManager.register(new ExplosivesCommand());
        commandManager.register(new RpgCommand());
    }

    @Override
    public void onReload() {
        rpgYml.save();
        rpgYml.reload();
    }

    public YamlConfig getRPGYml() {
        return rpgYml;
    }

}
