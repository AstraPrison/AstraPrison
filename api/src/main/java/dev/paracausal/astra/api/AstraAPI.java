package dev.paracausal.astra.api;

import dev.paracausal.astra.api.actions.ActionManager;
import dev.paracausal.astra.api.requirements.RequirementManager;

public interface AstraAPI {

    ActionManager getActionManager();
    RequirementManager getRequirementManager();

}
