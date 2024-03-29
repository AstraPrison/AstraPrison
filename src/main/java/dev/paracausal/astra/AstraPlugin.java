package dev.paracausal.astra;

import dev.paracausal.astra.api.AstraAPI;
import dev.paracausal.astra.api.actions.ActionManager;
import dev.paracausal.astra.api.actions.ActionManagerImpl;
import dev.paracausal.astra.api.requirements.RequirementManager;
import dev.paracausal.astra.api.requirements.RequirementManagerImpl;
import dev.paracausal.astra.commands.AdminCommand;
import dev.paracausal.astra.commands.CommandRegistry;
import dev.paracausal.astra.exceptions.InvalidCommandException;
import dev.paracausal.astra.listeners.MenuListener;
import dev.paracausal.astra.logger.AstraLog;
import dev.paracausal.astra.logger.AstraLogLevel;
import dev.paracausal.astra.utilities.configuration.YamlConfig;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AstraPlugin extends JavaPlugin implements AstraUtility, AstraAPI {

    private YamlConfig configYml;
    private ActionManagerImpl actionManager;
    private RequirementManagerImpl requirementManager;

    private List<Listener> listeners;
    private CommandRegistry commandRegistry;

    @Override
    public void onLoad() {
        configYml = new YamlConfig("config");
        AstraLog.onLoad();
        Astra.onLoad(this);

        actionManager = new ActionManagerImpl();
        actionManager.onLoad();

        requirementManager = new RequirementManagerImpl();
        requirementManager.onLoad();

        listeners = List.of(
                new MenuListener()
        );

        commandRegistry = new CommandRegistry();
    }

    @Override
    public void onEnable() {
        listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        try {
            commandRegistry.register(new AdminCommand());
        }

        catch (InvalidCommandException e) {
            AstraLog.debug(AstraLogLevel.ERROR, e.getMessage());
        }

        commandRegistry.onEnable();

        AstraLog.divider();
        AstraLog.log(AstraLogLevel.SUCCESS, "AstraPrison enabled!");
        AstraLog.log(
                "Version: " + getDescription().getVersion(),
                "Developed by Mantice"
        );
        AstraLog.divider();
    }

    @Override
    public void onDisable() {
        AstraLog.log(AstraLogLevel.SUCCESS, "AstraPrison disabled!");
    }

    @Override
    public ActionManager getActionManager() {
        return actionManager;
    }

    @Override
    public RequirementManager getRequirementManager() {
        return requirementManager;
    }

    @Override
    public YamlConfig getConfigYml() {
        return configYml;
    }

    @Override
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

}
