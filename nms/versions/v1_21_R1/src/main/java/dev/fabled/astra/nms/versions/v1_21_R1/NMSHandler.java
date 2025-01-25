package dev.fabled.astra.nms.versions.v1_21_R1;

import dev.fabled.astra.nms.AbstractFakeBlockHandler;
import dev.fabled.astra.nms.AbstractNMSHandler;
import dev.fabled.astra.nms.AbstractPacketListener;
import dev.fabled.astra.nms.AbstractPacketManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NMSHandler implements AbstractNMSHandler {

    private static NMSHandler instance;

    public static NMSHandler getInstance() {
        return instance;
    }

    private final @NotNull PacketManager packetManager;
    private final @NotNull PacketListener packetListener;

    public NMSHandler(final @NotNull JavaPlugin plugin) {
        instance = this;
        FakeBlockHandler.getInstance();
        packetManager = new PacketManager(plugin);
        packetListener = new PacketListener(packetManager);
    }

    @Override
    public AbstractFakeBlockHandler getFakeBlockHandler() {
        return FakeBlockHandler.getInstance();
    }

    @Override
    public AbstractPacketManager getPacketManager() {
        return packetManager;
    }

    @Override
    public AbstractPacketListener getPacketListener() {
        return packetListener;
    }

    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        if (!(packet instanceof Packet<?> p)) {
            return;
        }

        ((CraftPlayer) player).getHandle().connection.send(p);
    }

    @Override
    public boolean canBreakMaterial(
            final @NotNull Player player,
            final @Nullable ItemStack itemStack,
            final @NotNull Block block
    ) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return false;
        }

        final Location location = block.getLocation();

        final FakeBlockHandler fakeBlockHandler = FakeBlockHandler.getInstance();
        final Material material = fakeBlockHandler.isFakeBlock(player, location)
                ? fakeBlockHandler.getFakeBlock(player, location)
                : block.getType();

        final BlockState blockState = CraftMagicNumbers.getBlock(material).defaultBlockState();
        final Item item = CraftMagicNumbers.getItem(itemStack.getType());

        return item.getDestroySpeed(CraftItemStack.asNMSCopy(itemStack), blockState) > 1.0f;
    }

}
