package dev.paracausal.astra;

import dev.paracausal.astra.api.AstraAPI;
import dev.paracausal.astra.api.actions.ActionManager;
import dev.paracausal.astra.api.actions.ActionManagerImpl;
import dev.paracausal.astra.api.requirements.RequirementManager;
import dev.paracausal.astra.api.requirements.RequirementManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class AstraPlugin extends JavaPlugin implements AstraUtility, AstraAPI {

    private ActionManagerImpl actionManager;
    private RequirementManagerImpl requirementManager;

    @Override
    public void onLoad() {
        Astra.onLoad(this);

        actionManager = new ActionManagerImpl();
        actionManager.onLoad();

        requirementManager = new RequirementManagerImpl();
        requirementManager.onLoad();
    }

    @Override
    public void onEnable() {

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
