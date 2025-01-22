package dev.fabled.astra.nms.versions.v1_21_R2.impl;

import dev.fabled.astra.nms.AbstractPacket;
import dev.fabled.astra.nms.versions.v1_21_R2.FakeBlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class FakeBlockInteractPacket implements AbstractPacket {

    @Override
    public boolean run(final @NotNull Player player, final @NotNull Object packet) {
        if (!(packet instanceof ClientboundBlockUpdatePacket blockUpdatePacket)) {
            return true;
        }

        final BlockPos blockPos = blockUpdatePacket.getPos();
        return !FakeBlockHandler.getInstance().isFakeBlock(player, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public @NotNull Class<?> getPacketClass() {
        return ClientboundBlockUpdatePacket.class;
    }

}
