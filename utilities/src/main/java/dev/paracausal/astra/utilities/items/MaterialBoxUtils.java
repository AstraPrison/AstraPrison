package dev.paracausal.astra.utilities.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import dev.paracausal.astra.exceptions.InvalidMaterialException;
import dev.paracausal.astra.utilities.dependencies.Dependencies;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class MaterialBoxUtils {

    static ItemStack parseMaterial(
            @NotNull final MaterialBox materialBox,
            @Nullable OfflinePlayer player,
            @Nullable String templateReplacement
    ) {
        final MaterialBox.Type type = materialBox.getType();
        final String material = materialBox.getMaterial();

        if (type == MaterialBox.Type.MATERIAL) {
            return new ItemStack(Material.valueOf(material));
        }

        if (type == MaterialBox.Type.TEMPLATE) {
            if (templateReplacement == null) {
                return parseMaterial(ItemBuilder.DEFAULT_MATERIAL, player);
            }

            MaterialBox newBox;
            try { newBox = new MaterialBox(templateReplacement); }
            catch (InvalidMaterialException e) {
                newBox = ItemBuilder.DEFAULT_MATERIAL;
            }

            return parseMaterial(newBox, player);
        }

        if (type == MaterialBox.Type.HEAD_DATABASE_HEAD) {
            if (!Dependencies.HAS_HEAD_DATABASE) {
                return new ItemStack(Material.valueOf(material));
            }

            final String id = material.substring(4);
            return Dependencies.HEAD_DATABASE_API.getItemHead(id);
        }

        if (type == MaterialBox.Type.BASE64_HEAD) {
            final String texture = material.substring(7);

            final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.getProperties().add(new ProfileProperty("texture", texture));

            final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            final SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setPlayerProfile(profile);
            head.setItemMeta(meta);
            return head;
        }

        if (type == MaterialBox.Type.PLAYER_HEAD) {
            final String input = material.substring(7);

            UUID uuid = null;
            if (input.length() > 16) {
                try { uuid = UUID.fromString(input); }
                catch (IllegalArgumentException ignored) {}
            }

            final OfflinePlayer target = uuid != null
                    ? Bukkit.getOfflinePlayer(uuid)
                    : Bukkit.getOfflinePlayer(input);

            final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            final SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(target);
            head.setItemMeta(meta);
            return head;
        }

        return new ItemStack(Material.valueOf(material));
    }

    static public ItemStack parseMaterial(@NotNull final MaterialBox materialBox, @Nullable OfflinePlayer player) {
        return parseMaterial(materialBox, player, null);
    }

    static ItemStack parseMaterial(@NotNull final MaterialBox materialBox) {
        return parseMaterial(materialBox, null, null);
    }

}
