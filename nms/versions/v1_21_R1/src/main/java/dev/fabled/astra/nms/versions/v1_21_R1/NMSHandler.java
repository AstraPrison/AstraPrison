package dev.fabled.astra.nms.versions.v1_21_R1;

import dev.fabled.astra.nms.AbstractNMSHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NMSHandler implements AbstractNMSHandler {

    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        if (!(packet instanceof Packet<?> p)) {
            return;
        }

        ((CraftPlayer) player).getHandle().connection.send(p);
    }

    @Override
    public boolean canBreakMaterial(final @Nullable ItemStack itemStack, final @NotNull Material material) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return false;
        }

        final Block b = CraftMagicNumbers.getBlock(material);
        if (b == null) {
            return false;
        }

        final BlockState blockState = b.defaultBlockState();
        final Item item = CraftMagicNumbers.getItem(itemStack.getType());

        return item.getDestroySpeed(CraftItemStack.asNMSCopy(itemStack), blockState) > 1.0f;
    }

}
