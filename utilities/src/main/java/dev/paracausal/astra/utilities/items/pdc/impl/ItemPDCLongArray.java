package dev.paracausal.astra.utilities.items.pdc.impl;

import dev.paracausal.astra.utilities.items.pdc.ItemPDC;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemPDCLongArray extends ItemPDC<long[]> {

    public ItemPDCLongArray(@NotNull final NamespacedKey key, final long[] value) {
        super(key, value);
    }

    @Override
    public void modify(@NotNull PersistentDataContainer container) {
        container.set(key, PersistentDataType.LONG_ARRAY, value);
    }

}
