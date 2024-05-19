package dev.fabled.astra.omnitool;

import dev.fabled.astra.modules.impl.OmniToolModule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class OmniTool {

    public static void give(@NotNull final Player player) {
        final OmniToolModule module = OmniToolModule.getInstance();
        if (module == null) {
            return;
        }

        final ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        final ItemMeta meta = pickaxe.getItemMeta();
        meta.getPersistentDataContainer().set(module.getOmniToolKey(), PersistentDataType.BYTE, (byte) 1);
        pickaxe.setItemMeta(meta);

        player.getInventory().addItem(pickaxe);
    }

    public static boolean hasOmniTool(@NotNull final Player player) {
        final OmniToolModule module = OmniToolModule.getInstance();
        if (module == null) {
            return false;
        }

        final NamespacedKey key = module.getOmniToolKey();
        if (key == null) {
            return false;
        }

        for (final ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                continue;
            }

            if (item.getType() == Material.AIR) {
                continue;
            }

            final ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                continue;
            }

            final PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if (pdc.has(key, PersistentDataType.BYTE)) {
                return true;
            }
        }

        return false;
    }

}
