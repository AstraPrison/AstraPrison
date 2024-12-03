package dev.fabled.astra.utils.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import dev.fabled.astra.utils.dependencies.HdbUtils;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class MaterialUtils {

    public static final @NotNull Material DEFAULT_MATERIAL;

    static {
        DEFAULT_MATERIAL = Material.STONE;
    }

    public static @NotNull ItemStack parse(@NotNull String materialString, final @Nullable Player player) {
        materialString = PapiUtils.parse(materialString, player);
        final String lowerMaterial = materialString.toLowerCase();

        if (lowerMaterial.startsWith("hdb-") && HdbUtils.hasHeadDatabase()) {
            final HeadDatabaseAPI api = HdbUtils.getApi();
            assert api != null;

            final String id = materialString.substring(4);
            return api.getItemHead(id);
        }

        if (lowerMaterial.startsWith("base64-")) {
            final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            final String texture = materialString.substring(7);

            final PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
            playerProfile.setProperty(new ProfileProperty("textures", texture));

            final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setPlayerProfile(playerProfile);

            itemStack.setItemMeta(meta);
            return itemStack;
        }

        if (lowerMaterial.startsWith("player-")) {
            final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

            final String username = materialString.substring(7);
            final OfflinePlayer target = Bukkit.getOfflinePlayer(username);

            final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwningPlayer(target);

            itemStack.setItemMeta(meta);
            return itemStack;
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
