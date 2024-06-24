package dev.fabled.astra.omnitool.actions;

import dev.fabled.astra.listener.EnchantsTriggerEventListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class explode {

    public explode(Player player, Block block, String action) {
        World world = block.getWorld();
        int radius = 5;

        String[] parts = action.split(" ");
        if (parts.length > 1) {
            try {
                radius = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid radius for explode action: " + parts[1]);
            }
        }

        if (world == null) return;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radius * radius) {
                        Location explosionLocation = block.getLocation().clone().add(x, y, z);
                        if (EnchantsTriggerEventListener.isPlayerMap(player, block)) {
                            player.sendBlockChange(explosionLocation, Material.AIR.createBlockData());
                        }
                    }
                }
            }
        }
    }
}
