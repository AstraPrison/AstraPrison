package dev.fabled.astra.utils.items.pdc;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public abstract class ItemPDC<T> {

    protected final @NotNull NamespacedKey key;
    protected final @NotNull T value;

    public ItemPDC(final @NotNull NamespacedKey key, final @NotNull T value) {
        this.key = key;
        this.value = value;
    }

    public abstract void apply(final @NotNull PersistentDataContainer pdc);

}
