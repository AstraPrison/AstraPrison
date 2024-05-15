package dev.fabled.astra.omnitool;

import dev.fabled.astra.modules.impl.OmniToolModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    }

}
