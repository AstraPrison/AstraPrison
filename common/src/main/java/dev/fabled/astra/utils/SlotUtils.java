package dev.fabled.astra.utils;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SlotUtils {

    @Contract("_, _, !null -> !null")
    public static @Nullable Set<Integer> fromConfig(
            final @NotNull YamlConfig config,
            final @NotNull String key,
            final @Nullable Set<Integer> def
    ) {
        final List<String> slotList = ListUtils.fromConfig(config, key);
        if (slotList == null) {
            return def;
        }

        final Set<Integer> slots = new HashSet<>();

        slotList.forEach(string -> {
            if (!string.contains("-")) {
                try { slots.add(Integer.parseInt(string)); }
                catch (NumberFormatException ignored) {}
                return;
            }

            final String[] split = string.split("-");
            final int min, max;

            try {
                min = Integer.parseInt(split[0]);
                if (split.length < 2) {
                    slots.add(min);
                    return;
                }

                max = Integer.parseInt(split[1]);
            }
            catch (NumberFormatException e) {
                return;
            }

            for (int i = min; i <= max; i++) {
                slots.add(i);
            }
        });

        return slots;
    }

    public static @Nullable Set<Integer> fromConfig(final @NotNull YamlConfig config, final @NotNull String key) {
        return fromConfig(config, key, null);
    }

}
