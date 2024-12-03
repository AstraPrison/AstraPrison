package dev.fabled.astra.utils.items.pdc;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class ShortPDC extends ItemPDC<Short> {

    public ShortPDC(final @NotNull NamespacedKey key, final short value) {
        super(key, value);
    }

    @Override
    public void apply(final @NotNull PersistentDataContainer pdc) {
        pdc.set(key, PersistentDataType.SHORT, value);
    }

}
