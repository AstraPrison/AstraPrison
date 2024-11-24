package dev.fabled.astra.utils.items;

import dev.fabled.astra.utils.dependencies.HdbUtils;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MaterialUtils {

    public static final @NotNull Material DEFAULT_MATERIAL;

    static {
        DEFAULT_MATERIAL = Material.STONE;
    }

    public static @NotNull ItemStack parse(@NotNull String materialString, final @Nullable Player player) {
        materialString = PapiUtils.parse(materialString, player);

        if (materialString.startsWith("hdb-") && HdbUtils.hasHeadDatabase()) {
            final HeadDatabaseAPI api = HdbUtils.getApi();
            assert api != null;

            final String id = materialString.substring(4);
            return api.getItemHead(id);
        }

        Material material;
        try { material = Material.valueOf(materialString); }
        catch (IllegalArgumentException e) {
            material = DEFAULT_MATERIAL;
        }

        return new ItemStack(material);
    }

    public static @NotNull ItemStack parse(final @NotNull String material) {
        return parse(material, null);
    }

}
