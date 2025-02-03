package dev.fabled.astra.menus.requirements;

import dev.fabled.astra.api.requirements.ClickRequirementOptions;
import dev.fabled.astra.api.requirements.ClickRequirementOptionsImpl;
import dev.fabled.astra.menus.actions.ItemClickActions;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

public final class RequirementActions {

    final @NotNull ClickRequirementOptions requirementOptions;
    final @NotNull ItemClickActions clickActions;

    public RequirementActions(final @NotNull YamlConfig config, final @NotNull String key) {
        requirementOptions = new ClickRequirementOptionsImpl(config, key);
        clickActions = new ItemClickActions(config, key + ".actions");
    }

}
