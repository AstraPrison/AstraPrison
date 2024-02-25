package dev.paracausal.astra.utilities.items;

import dev.paracausal.astra.exceptions.InvalidMaterialException;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public final class MaterialBox {

    public enum Type {
        MATERIAL,
        TEMPLATE,
        PLAYER_HEAD,
        HEAD_DATABASE_HEAD,
        BASE64_HEAD
    }

    private Type type;
    private @NotNull final String material;

    public MaterialBox(@NotNull final Material material) {
        type = Type.MATERIAL;
        this.material = material.toString();
    }

    public MaterialBox(@NotNull final MaterialBox materialBox) {
        type = materialBox.type;
        this.material = materialBox.material;
    }

    public MaterialBox(@NotNull final String material) throws InvalidMaterialException {
        final String lower = material.toLowerCase();

        if (lower.startsWith("hdb-")) {
            type = Type.HEAD_DATABASE_HEAD;
        }

        if (lower.startsWith("base64-")) {
            type = Type.BASE64_HEAD;
        }

        if (lower.startsWith("player-")) {
            type = Type.PLAYER_HEAD;
        }

        if (material.contains("{") && material.contains("}")) {
            type = Type.TEMPLATE;
        }

        if (type == null) {
            type = Type.MATERIAL;
            try { Material.valueOf(material); }
            catch (IllegalArgumentException e) {
                throw new InvalidMaterialException();
            }
        }

        this.material = material;
    }

    public @NotNull String getMaterial() {
        return material;
    }

    public @NotNull Type getType() {
        return type;
    }

}
