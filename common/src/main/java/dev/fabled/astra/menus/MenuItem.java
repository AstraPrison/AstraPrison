package dev.fabled.astra.menus;

import dev.fabled.astra.utils.SlotUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.items.ItemBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class MenuItem extends ItemBuilder {

    private @Nullable Set<Integer> slots;

    public MenuItem(@NotNull String material) {
        super(material);
    }

    public MenuItem(final @NotNull YamlConfig config, @NotNull String key) {
        super(config, key);
        if (!key.endsWith(".")) {
            key += ".";
        }

        slots = SlotUtils.fromConfig(config, key + "slots");
    }

    public @NotNull MenuItem setSlots(final @Nullable Set<Integer> slots) {
        if (slots == null) {
            this.slots = null;
            return this;
        }

        this.slots = new HashSet<>(slots);
        return this;
    }

    public @NotNull MenuItem setSlots(final @NotNull Integer... slots) {
        return setSlots(Set.of(slots));
    }

    public @NotNull MenuItem addSlots(final @NotNull Set<Integer> slots) {
        if (this.slots == null) {
            this.slots = new HashSet<>(slots);
            return this;
        }

        this.slots.addAll(slots);
        return this;
    }

    public @NotNull MenuItem addSlots(final @NotNull Integer... slots) {
        return addSlots(Set.of(slots));
    }

    public @NotNull Set<Integer> getSlots() {
        if (slots == null) {
            return new HashSet<>();
        }

        return new HashSet<>(slots);
    }

}
