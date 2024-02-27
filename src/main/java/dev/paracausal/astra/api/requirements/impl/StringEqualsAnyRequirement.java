package dev.paracausal.astra.api.requirements.impl;

import dev.paracausal.astra.api.requirements.Requirement;
import dev.paracausal.astra.api.requirements.RequirementOptions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StringEqualsAnyRequirement extends Requirement {

    public StringEqualsAnyRequirement() {
        super("any string equals", "string equals any");
    }

    @Override
    public boolean check(@NotNull Player player, RequirementOptions options) {
        final List<String> output = options.getStringList("output", new ArrayList<>());
        if (output.isEmpty()) {
            return false;
        }

        final String input = options.getString("input");
        if (input == null) {
            return false;
        }

        final boolean inverted = options.getBoolean("inverted", false);
        final boolean ignoreCase = options.getBoolean("ignore-case", false);

        for (final String out : output) {
            if (ignoreCase && out.equalsIgnoreCase(input)) {
                return !inverted;
            }

            else if (out.equals(input)) {
                return !inverted;
            }
        }

        return inverted;
    }

}
