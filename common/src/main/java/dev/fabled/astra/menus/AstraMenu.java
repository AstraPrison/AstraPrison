package dev.fabled.astra.menus;

import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AstraMenu implements InventoryHolder {

    /*
            TODO
            Pagination
     */

    final int size;
    private final @NotNull String title;
    private final @NotNull Map<String, MenuItem> items;

    public AstraMenu(final @NotNull YamlConfig config) {
        int s = config.options().getInt("size", 54);
        s = Math.max(0, s);
        s = Math.min(54, s);

        if (s % 9 != 0) {
            s -= s % 9;
        }

        size = s;
        title = config.options().getString("title", "Astra Menu");
        items = new HashMap<>();

        final ConfigurationSection section = config.options().getConfigurationSection("contents");
        if (section == null) {
            return;
        }

        section.getKeys(false).forEach(key ->
                items.put(key, new MenuItem(config, "contents." + key + ".", key))
        );
    }

    public @NotNull Inventory getInventory(final @Nullable Player player) {
        final Inventory inventory = Bukkit.createInventory(this, size,
                MiniColor.INVENTORY.deserialize(PapiUtils.parse(title, player))
        );

        items.forEach((key, menuItem) -> {
            final ItemStack item = menuItem.build(player);
            menuItem.getSlots().forEach(slot -> {
                if (slot > size - 1 || slot < 0) {
                    return;
                }

                inventory.setItem(slot, item);
            });
        });

        return inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getInventory(null);
    }

    public static void closeAll() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof AstraMenu)) {
                return;
            }

            player.closeInventory();
        });
    }

}
