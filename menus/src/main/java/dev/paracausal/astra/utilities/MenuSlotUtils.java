package dev.paracausal.astra.utilities;

import dev.paracausal.astra.utilities.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MenuSlotUtils {

    public static @NotNull List<Integer> fromConfig(@NotNull final YamlConfig config, @NotNull final String path) {
        final List<String> stringSlots = ListUtils.fromConfig(config, path);
        final List<Integer> slots = new ArrayList<>();

        stringSlots.forEach(string -> {
            final String[] split = string.split("-");

            if (!string.contains("-") || split.length < 1) {
                final int slot;
                try { slot = Integer.parseInt(string); }
                catch (NumberFormatException e) {
                    return;
                }

                if (slots.contains(slot)) {
                    return;
                }

                slots.add(slot);
                return;
            }

            final int one, two;
            try {
                one = Integer.parseInt(split[0]);
                two = Integer.parseInt(split[1]);
            }
            catch (NumberFormatException e) {
                return;
            }

            if (one == two) {
                if (!slots.contains(one)) {
                    slots.add(one);
                }

                return;
            }

            final int min, max;
            min = Math.min(one, two);
            max = Math.max(one, two);

            for (int i = min; i <= max; i++) {
                if (slots.contains(i)) {
                    continue;
                }

                slots.add(i);
            }
        });

        return slots;
    }

}
