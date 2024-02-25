package dev.paracausal.astra.utilities.items;

import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class ItemFlagUtils {

    static @Nullable List<ItemFlag> parse(@Nullable final List<String> input) {
        if (input == null) {
            return null;
        }

        final List<ItemFlag> flags = new ArrayList<>();

        input.forEach(string -> {
            try { flags.add(ItemFlag.valueOf(string)); }
            catch (IllegalArgumentException ignored) {}
        });

        return flags;
    }

}
