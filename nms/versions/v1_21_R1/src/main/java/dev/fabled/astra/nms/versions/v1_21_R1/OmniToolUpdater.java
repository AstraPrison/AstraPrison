package dev.fabled.astra.nms.versions.v1_21_R1;

import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.nms.AbstractOmniToolUpdater;
import dev.fabled.astra.omnitool.OmniTool;
import dev.fabled.astra.omnitool.levels.OmniToolLevel;
import dev.fabled.astra.omnitool.levels.OmniToolLevelManager;
import dev.fabled.astra.omnitool.levels.OmniToolType;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.tools.ToolType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class OmniToolUpdater implements AbstractOmniToolUpdater {

    private static OmniToolUpdater instance;

    public static @NotNull OmniToolUpdater getInstance() {
        if (instance == null) {
            instance = new OmniToolUpdater();
        }

        return instance;
    }

    private final OmniToolLevelManager levelManager;

    private OmniToolUpdater() {
        levelManager = OmniToolModule.getInstance().getLevelManager();
    }

    @Override
    public void update(
            final @NotNull Player player,
            final @NotNull ItemStack itemStack,
            final @NotNull BlockState blockState
    ) {
        if (itemStack.getType().isAir()) {
            return;
        }

        final ItemMeta meta = itemStack.getItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        final int level = container.getOrDefault(OmniTool.getOmniToolKey(), PersistentDataType.INTEGER, 0);

        final ToolType preferredTool = ToolType.getPreferredToolType(blockState);
        final OmniToolLevel omniToolLevel = levelManager.getLevel(level);

        final OmniToolType toolType;
        switch (preferredTool) {
            case SHOVEL -> toolType = omniToolLevel.getShovel();
            case AXE -> toolType = omniToolLevel.getAxe();
            default -> toolType = omniToolLevel.getPickaxe();
        }

        itemStack.setType(toolType.getMaterial());
        meta.setCustomModelData(toolType.getCustomModelData());
        meta.displayName(MiniColor.INVENTORY.deserialize(toolType.getDisplayName()));
        itemStack.setItemMeta(meta);
    }

}
