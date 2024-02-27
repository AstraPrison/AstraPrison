package dev.paracausal.astra.api.requirements.impl;

import dev.paracausal.astra.api.requirements.Requirement;
import dev.paracausal.astra.api.requirements.RequirementOptions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HasPermissionRequirement extends Requirement {

    public HasPermissionRequirement() {
        super("has permission", "permission");
    }

    @Override
    public boolean check(@NotNull Player player, RequirementOptions options) {
        final String permission = options.getString("permission");
        if (permission == null) {
            return false;
        }

        final boolean inverted = options.getBoolean("inverted", false);
        final boolean allowed = player.hasPermission(permission);

        return allowed != inverted;
    }

}
