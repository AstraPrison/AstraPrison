package dev.fabled.astra.nms.versions.v1_21_R3.impl;

import dev.fabled.astra.nms.AbstractPacket;
import dev.fabled.astra.nms.versions.v1_21_R3.FakeBlockHandler;
import dev.fabled.astra.nms.versions.v1_21_R3.OmniToolUpdater;
import dev.fabled.astra.omnitool.OmniTool;
import dev.fabled.astra.utils.tools.ToolSpeed;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class FakeBlockMinePacket implements AbstractPacket {

    private final @NotNull JavaPlugin plugin;

    public FakeBlockMinePacket(final @NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean run(@NotNull Player player, @NotNull Object packet) {
        if (!(packet instanceof ServerboundPlayerActionPacket playerActionPacket)) {
            return true;
        }

        final ServerboundPlayerActionPacket.Action action = playerActionPacket.getAction();
        if (
                action != ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK
                        && action != ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK
        ) {
            return true;
        }

        final PlayerInventory inventory = player.getInventory();
        final ItemStack itemStack = inventory.getItemInMainHand();

        final BlockPos blockPos = playerActionPacket.getPos();
        final int x, y, z;
        x = blockPos.getX();
        y = blockPos.getY();
        z = blockPos.getZ();
        final Block block = player.getWorld().getBlockAt(x, y, z);

        final BlockState blockState;
        try { blockState = block.getState(); }
        catch (IllegalStateException e) {
            return true;
        }

        final FakeBlockHandler fakeBlockHandler = FakeBlockHandler.getInstance();
        Material mat = fakeBlockHandler.getFakeBlock(player, x, y, z);
        final boolean isFakeBlock = mat != null;
        mat = isFakeBlock ? mat : blockState.getType();
        blockState.setType(mat);

        if (action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK && OmniTool.isOmniTool(itemStack)) {
            OmniToolUpdater.getInstance().update(player, itemStack, blockState);
        }

        if (!isFakeBlock) {
            return true;
        }

        final double timeToBreak = ToolSpeed.calculate(player, itemStack, blockState);
        if (action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK && timeToBreak > 0) {
            return true;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            final Collection<ItemStack> drops = blockState.getDrops(itemStack);
            drops.forEach(inventory::addItem);

            blockState.setType(block.getType());
            fakeBlockHandler.removeFakeBlock(player, x, y, z);
            player.sendBlockChange(block.getLocation(), blockState.getBlockData());
        }, 1L);

        return true;
    }

    @Override
    public @NotNull Class<?> getPacketClass() {
        return ServerboundPlayerActionPacket.class;
    }
}
