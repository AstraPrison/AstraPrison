package dev.fabled.astra.omnitool.actions;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class spawn {

    public spawn(Player player, Block block, String action) {
        World world = block.getWorld();
        Location location = block.getLocation().add(0.5, 1, 0.5);

        if (world == null) return;

        UUID entityUUID = UUID.randomUUID();
        int entityID = (int) (Math.random() * Integer.MAX_VALUE);

        Vector3d position = new Vector3d(location.getX(), location.getY(), location.getZ());
        Vector3d velocity = new Vector3d(0, 0, 0);
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        float headPitch = pitch;
        float headYaw = yaw;
        int data = 0;

        EntityType entityType = action.contains("spawn") ? EntityTypes.getByName(action.split(" ")[1]) : EntityTypes.SHEEP;

        WrapperPlayServerSpawnEntity spawnEntityPacket = new WrapperPlayServerSpawnEntity(
                entityID, Optional.of(entityUUID), entityType, position, pitch, yaw, headYaw, data, Optional.of(velocity));

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnEntityPacket);

        player.sendMessage("Ein explosives Schaf wurde nur für dich gespawnt!");
    }
}
