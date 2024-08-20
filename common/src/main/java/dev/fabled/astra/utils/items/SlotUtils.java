package dev.fabled.astra.utils.items;

import dev.fabled.astra.utils.parsers.NumberParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlotUtils {

    public static @NotNull SlotSet parse(@Nullable final List<String> input) {
        if (input == null) {
            return new SlotSet();
        }

        final SlotSet set = new SlotSet();

        input.forEach(string -> {
            final String[] split = string.split("-");

            if (split.length < 1) {
                set.add(NumberParser.parse(string, Integer::parseInt, null));
                return;
            }

            int one, two, min, max;
            one = NumberParser.parseOrDefault(split[0], Integer::parseInt, 0);
            two = NumberParser.parseOrDefault(split[1], Integer::parseInt, 0);

            if (one == two) {
                set.add(one);
                return;
            }

            min = Math.min(one, two);
            max = Math.max(one, two);

            for (int i = min; i <= max; i++) {
                set.add(i);
            }
        });

        return set;
    }

}
