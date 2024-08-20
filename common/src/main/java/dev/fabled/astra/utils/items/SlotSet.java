package dev.fabled.astra.utils.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SlotSet extends HashSet<Integer> {

    public SlotSet(@NotNull final Set<Integer> slots) {
        super();
        this.addAll(slots);
    }

    public SlotSet() {
        super();
    }

    @Override
    public boolean add(@Nullable Integer value) {
        if (value == null) {
            return false;
        }

        if (value < 0 || value > 53) {
            return false;
        }

        return super.add(value);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        boolean modified = false;

        for (Integer e : c) {
            if (this.add(e)) {
                modified = true;
            }
        }

        return modified;
    }

}
