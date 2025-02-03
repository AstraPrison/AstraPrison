package dev.fabled.astra.menus;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.SlotUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.items.ItemBuilder;
import dev.fabled.astra.utils.items.pdc.StringPDC;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class MenuItem extends ItemBuilder {

    private final @NotNull String id;
    private @Nullable Set<Integer> slots;

    public MenuItem(final @NotNull String id, final @NotNull String material) {
        super(material);
        this.id = id;
    }

    public MenuItem(final @NotNull YamlConfig config, @NotNull String key, final @NotNull String id) {
        super(config, key);
        this.id = id;
        if (!key.endsWith(".")) {
            key += ".";
        }

        slots = SlotUtils.fromConfig(config, key + "slots");

        addPDC(new StringPDC(Astra.getAstraMenuItemKey(), id));
    }

    public final @NotNull String getId() {
        return id;
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
