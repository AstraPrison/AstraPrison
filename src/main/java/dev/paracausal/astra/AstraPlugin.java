package dev.paracausal.astra;

import dev.paracausal.astra.api.AstraAPI;
import dev.paracausal.astra.api.actions.ActionManager;
import dev.paracausal.astra.api.actions.ActionManagerImpl;
import dev.paracausal.astra.api.requirements.RequirementManager;
import dev.paracausal.astra.api.requirements.RequirementManagerImpl;
import dev.paracausal.astra.listeners.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AstraPlugin extends JavaPlugin implements AstraUtility, AstraAPI {

    private ActionManagerImpl actionManager;
    private RequirementManagerImpl requirementManager;

    private List<Listener> listeners;

    @Override
    public void onLoad() {
        Astra.onLoad(this);

        actionManager = new ActionManagerImpl();
        actionManager.onLoad();

        requirementManager = new RequirementManagerImpl();
        requirementManager.onLoad();

        listeners = List.of(
                new MenuListener()
        );
    }

    @Override
    public void onEnable() {
        listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {

    }

    @Override
    public ActionManager getActionManager() {
        return actionManager;
    }

    @Override
    public RequirementManager getRequirementManager() {
        return requirementManager;
    }

}
