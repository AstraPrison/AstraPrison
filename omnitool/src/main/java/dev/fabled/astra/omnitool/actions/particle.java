package dev.fabled.astra.omnitool.actions;

import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class particle {

    public particle(Player player, Block block, String action) {
        player.spawnParticle(Particle.valueOf(action), block.getX(), block.getY(), block.getZ(), 1, 0, 0, 0, 0);


    }
}
