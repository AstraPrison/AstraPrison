package dev.fabled.astra.utils.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import dev.fabled.astra.exceptions.InvalidMaterialException;
import dev.fabled.astra.utils.dependencies.HeadDatabaseUtils;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
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

public class MaterialBuilder {

    public static @NotNull ItemStack build(
            @NotNull final MaterialBox materialBox,
            @Nullable final Player player,
            @Nullable final String variableValue
    ) {
        final MaterialBox.Type type = materialBox.getType();

        if (type == MaterialBox.Type.VARIABLE) {
            if (variableValue == null) {
                return ItemStackUtils.getDefaultItemStack();
            }

            try {
                return build(new MaterialBox(PapiUtils.parse(player, variableValue)), player, null);
            }
            catch (InvalidMaterialException e) {
                AstraLog.log(AstraLogLevel.ERROR, "Invalid material: " + variableValue);
                AstraLog.log(e);
                return ItemStackUtils.getDefaultItemStack();
            }
        }

        if (type == MaterialBox.Type.PLAYER_HEAD) {
            final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            final SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta == null) {
                return head;
            }

            final String sub = materialBox.getSubMaterial();
            if (sub == null) {
                return head;
            }

            String subMaterial = PapiUtils.parse(player, sub);
            if (player != null) {
                subMaterial = subMaterial.replace("{PLAYER}", player.getName())
                        .replace("{UUID}", player.getUniqueId().toString());
            }

            final UUID uuid = subMaterial.length() > 16
                    ? UUID.fromString(subMaterial)
                    : null;

            final OfflinePlayer target = uuid == null
                    ? Bukkit.getOfflinePlayer(subMaterial)
                    : Bukkit.getOfflinePlayer(uuid);

            meta.setOwningPlayer(target);
            head.setItemMeta(meta);
            return head;
        }

        if (type == MaterialBox.Type.BASE64_HEAD) {
            final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            final SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta == null) {
                return head;
            }

            final String sub = materialBox.getSubMaterial();
            if (sub == null) {
                return head;
            }

            final String texture = PapiUtils.parse(player, sub);
            final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", texture));

            meta.setPlayerProfile(profile);
            head.setItemMeta(meta);
            return head;
        }

        if (type == MaterialBox.Type.HEAD_DATABASE) {
            final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            String id = materialBox.getSubMaterial();
            if (id == null) {
                return head;
            }

            id = PapiUtils.parse(player, id);
            final HeadDatabaseAPI api = HeadDatabaseUtils.getHeadDatabaseAPI();

            return api != null
                    ? api.getItemHead(id)
                    : head;
        }

        final String material = PapiUtils.parse(player, materialBox.getMaterial());

        try {
            return new ItemStack(Material.valueOf(material));
        }
        catch (IllegalArgumentException e) {
            AstraLog.log(AstraLogLevel.ERROR, "Invalid material: " + material);
            AstraLog.log(e);
            return ItemStackUtils.getDefaultItemStack();
        }
    }

    public static @NotNull ItemStack build(@NotNull final MaterialBox materialBox, @Nullable final Player player) {
        return build(materialBox, player, null);
    }

    public static @NotNull ItemStack build(@NotNull final MaterialBox materialBox) {
        return build(materialBox, null, null);
    }

}
