package dev.fabled.astra.utils.items.pdc;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public abstract class ItemPDC<T> {

    protected @NotNull final NamespacedKey key;
    protected @NotNull final T value;

    protected ItemPDC(@NotNull final NamespacedKey key, @NotNull final T value) {
        this.key = key;
        this.value = value;
    }

    public @NotNull NamespacedKey getKey() {
        return key;
    }

    public @NotNull T getValue() {
        return value;
    }

    public abstract void modify(@NotNull final PersistentDataContainer container);

}
