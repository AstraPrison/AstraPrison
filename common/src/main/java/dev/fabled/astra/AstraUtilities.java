package dev.fabled.astra;

import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.menus.MenuManager;
import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.utils.configuration.YamlConfig;

public interface AstraUtilities {

    YamlConfig getConfigYml();

    MenuManager getMenuManager();
    CommandManager getCommandManager();
    ModuleManager getModuleManager();

}
