package dev.paracausal.astra.api.requirements.impl;

import dev.paracausal.astra.api.requirements.Requirement;
import dev.paracausal.astra.api.requirements.RequirementOptions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StringEqualsRequirement extends Requirement {

    public StringEqualsRequirement() {
        super("string equals");
    }

    @Override
    public boolean check(@NotNull Player player, RequirementOptions options) {
        final String input = options.getString("input");
        final String output = options.getString("output");

        if (input == null || output == null) {
            return false;
        }

        final boolean ignoreCase = options.getBoolean("ignore-case", false);

        return ignoreCase
                ? input.equalsIgnoreCase(output)
                : input.equals(output);
    }

}
