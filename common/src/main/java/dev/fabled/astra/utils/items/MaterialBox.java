package dev.fabled.astra.utils.items;

import dev.fabled.astra.exceptions.InvalidMaterialException;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MaterialBox {

    public static @NotNull MaterialBox copy(@NotNull final MaterialBox materialBox) {
        return new MaterialBox(materialBox);
    }

    public enum Type {
        MATERIAL,
        PLAYER_HEAD,
        BASE64_HEAD,
        HEAD_DATABASE,
        VARIABLE
    }

    private @NotNull final String material;
    private @Nullable final String subMaterial;
    private @NotNull final Type type;

    public MaterialBox(@NotNull final MaterialBox materialBox) {
        material = materialBox.material;
        subMaterial = materialBox.subMaterial;
        type = materialBox.type;
    }

    public MaterialBox(@NotNull final Material material) {
        this.material = material.toString();
        subMaterial = this.material;
        type = Type.MATERIAL;
    }

    public MaterialBox(@NotNull final String material) throws InvalidMaterialException {
        final String lower = material.toLowerCase();
        this.material = material;

        if (lower.startsWith("{") && lower.startsWith("}")) {
            type = Type.VARIABLE;
            subMaterial = material;
            return;
        }

        if (lower.startsWith("player-")) {
            subMaterial = material.substring(7);
            type = Type.PLAYER_HEAD;
            return;
        }

        if (lower.startsWith("base64-")) {
            subMaterial = material.substring(7);
            type = Type.BASE64_HEAD;
            return;
        }

        if (lower.startsWith("hdb-")) {
            subMaterial = material.substring(4);
            type = Type.HEAD_DATABASE;
            return;
        }

        try { Material.valueOf(material); }
        catch (IllegalArgumentException e) {
            throw new InvalidMaterialException();
        }

        subMaterial = material;
        type = Type.MATERIAL;
    }

    public @NotNull String getMaterial() {
        return material;
    }

    public @Nullable String getSubMaterial() {
        return subMaterial;
    }

    public @NotNull Type getType() {
        return type;
    }

}
