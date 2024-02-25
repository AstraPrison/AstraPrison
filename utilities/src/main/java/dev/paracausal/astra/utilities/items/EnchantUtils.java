package dev.paracausal.astra.utilities.items;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class EnchantUtils {

    static @Nullable Map<Enchantment, Integer> parse(@Nullable final List<String> input) {
        if (input == null) {
            return null;
        }

        final Map<Enchantment, Integer> map = new HashMap<>();

        input.forEach(string -> {
            Enchantment enchantment = getEnchant(string);
            if (enchantment == null) {
                return;
            }

            map.put(enchantment, getLevel(string));
        });

        return map;
    }

    @SuppressWarnings("deprecation")
    static @Nullable Enchantment getEnchant(@NotNull final String input) {
        Enchantment enchantment = null;

        if (input.contains(" ")) {
            String[] split = input.split(" ");

            for (final String s : split) {
                if (enchantment != null) {
                    break;
                }

                enchantment = Enchantment.getByName(s);
            }
        }

        else {
            enchantment = Enchantment.getByName(input);
        }

        return enchantment;
    }

    static int getLevel(@NotNull final String input) {
        if (!input.contains(" ")) {
            try { return Integer.parseInt(input); }
            catch (NumberFormatException e) {
                return 1;
            }
        }

        int level = -1;
        String[] split = input.split(" ");

        for (final String s : split) {
            try {
                level = Integer.parseInt(s);
                break;
            }
            catch (NumberFormatException ignored) {}
        }

        return Math.max(1, level);
    }

}
