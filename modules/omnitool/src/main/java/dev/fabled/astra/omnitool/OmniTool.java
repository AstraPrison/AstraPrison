package dev.fabled.astra.omnitool;

import dev.fabled.astra.Astra;
import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.omnitool.levels.OmniToolLevel;
import dev.fabled.astra.omnitool.levels.OmniToolLevelManager;
import dev.fabled.astra.omnitool.levels.OmniToolType;
import dev.fabled.astra.utils.MiniColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OmniTool {

    private static NamespacedKey omniToolKey;
    private static ItemStack defOmniTool;

    public static void onLoad() {
        omniToolKey = new NamespacedKey(Astra.getPlugin(), "astra-omnitool");
        onReload();
    }

    public static void onReload() {
        final OmniToolLevelManager manager = OmniToolModule.getInstance().getLevelManager();
        final OmniToolLevel level = manager.getLevel(0);
        final OmniToolType pickaxe = level.getPickaxe();

        defOmniTool = new ItemStack(pickaxe.getMaterial());
        final ItemMeta meta = defOmniTool.getItemMeta();

        meta.setCustomModelData(pickaxe.getCustomModelData());
        meta.displayName(MiniColor.INVENTORY.deserialize(pickaxe.getDisplayName()));
        meta.getPersistentDataContainer().set(omniToolKey, PersistentDataType.INTEGER, 0);

        meta.setUnbreakable(true);
        meta.addItemFlags(
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ATTRIBUTES
        );

        defOmniTool.setItemMeta(meta);
    }

    public static NamespacedKey getOmniToolKey() {
        return omniToolKey;
    }

    public static @NotNull ItemStack getDefaultOmniTool() {
        return defOmniTool.clone();
    }

    public static boolean isOmniTool(final @Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return false;
        }

        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(omniToolKey);
    }

    public static boolean hasOmniTool(final @NotNull Player player) {
        final PlayerInventory inventory = player.getInventory();
        for (final ItemStack itemStack : inventory.getContents()) {
            if (isOmniTool(itemStack)) {
                return true;
            }
        }

        return false;
    }

}
