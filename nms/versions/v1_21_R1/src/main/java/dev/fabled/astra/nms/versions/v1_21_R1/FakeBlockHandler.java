package dev.fabled.astra.nms.versions.v1_21_R1;

import dev.fabled.astra.nms.AbstractFakeBlockHandler;
import dev.fabled.astra.nms.regions.AbstractRegion;
import dev.fabled.astra.utils.WeightedList;
import dev.fabled.astra.utils.blocks.BlockMaterial;
import dev.fabled.astra.utils.blocks.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public final class FakeBlockHandler implements AbstractFakeBlockHandler {

    private static FakeBlockHandler instance;

    public static FakeBlockHandler getInstance() {
        if (instance == null) {
            instance = new FakeBlockHandler();
        }

        return instance;
    }

    private final @NotNull Map<UUID, Map<Vector3i, Material>> materials;

    private FakeBlockHandler() {
        materials = new HashMap<>();
    }

    @Override
    public void setBlock(
            final @NotNull Player player,
            final @NotNull BlockMaterial material,
            final @NotNull Location location
    ) {
        final Material mat = material.getMaterial();
        final BlockData blockData = mat.createBlockData();
        player.sendBlockChange(location, blockData);
        addFakeBlock(player.getUniqueId(), location, mat);
    }

    @Override
    public void fillRegion(
            final @NotNull Player player,
            final @NotNull BlockMaterial material,
            final @NotNull AbstractRegion region
    ) {
        final Map<BlockPosition, BlockData> blockDataMap = new HashMap<>();
        final Material mat = material.getMaterial();
        final BlockData blockData = mat.createBlockData();

        region.getLocations().forEach(loc ->
            blockDataMap.put(new BlockPosition(loc[0], loc[1], loc[2]), blockData)
        );

        player.sendMultiBlockChange(blockDataMap);
        addFakeBlocks(player.getUniqueId(), new HashMap<>(blockDataMap));
    }

    @Override
    public void fillRegion(
            final @NotNull Player player,
            final @NotNull WeightedList<BlockMaterial> materials,
            final @NotNull AbstractRegion region
    ) {
        final Map<BlockPosition, BlockData> blockDataMap = new HashMap<>();

        region.getLocations().forEach(loc -> {
            final BlockMaterial material = materials.next();

            final Material mat = material == null
                    ? Material.STONE
                    : material.getMaterial();

            final BlockData blockData = mat.createBlockData();
            blockDataMap.put(new BlockPosition(loc[0], loc[1], loc[2]), blockData);
        });

        player.sendMultiBlockChange(blockDataMap);
        addFakeBlocks(player.getUniqueId(), new HashMap<>(blockDataMap));
    }

    @Override
    public boolean isFakeBlock(final @NotNull UUID uuid, final int x, final int y, final int z) {
        if (!materials.containsKey(uuid)) {
            return false;
        }

        return materials.get(uuid).containsKey(new Vector3i(x, y, z));
    }

    @Override
    public @NotNull Map<Vector3i, Material> getFakeBlocks(final @NotNull UUID uuid) {
        if (!materials.containsKey(uuid)) {
            return new HashMap<>();
        }

        return new HashMap<>(materials.get(uuid));
    }

    @Override
    public @Nullable Material getFakeBlock(final @NotNull UUID uuid, final int x, final int y, final int z) {
        return materials.getOrDefault(uuid, new HashMap<>()).getOrDefault(new Vector3i(x, y, z), null);
    }

    @Override
    public void clear(final @NotNull UUID uuid) {
        materials.remove(uuid);
    }

    private @NotNull Map<Vector3i, Material> getOrCreate(final @NotNull UUID uuid) {
        final Map<Vector3i, Material> map;
        if (materials.containsKey(uuid)) {
            map = materials.get(uuid);
        }

        else {
            map = new HashMap<>();
            materials.put(uuid, map);
        }

        return map;
    }

    private void addFakeBlock(
            final @NotNull UUID uuid,
            final @NotNull Location location,
            final @NotNull Material material
    ) {
        getOrCreate(uuid).put(new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ()), material);
    }

    public void removeFakeBlock(final @NotNull UUID uuid, final int x, final int y, final int z) {
        getOrCreate(uuid).remove(new Vector3i(x, y, z));
    }

    public void removeFakeBlock(final @NotNull Player player, final int x, final int y, final int z) {
        removeFakeBlock(player.getUniqueId(), x, y, z);
    }

    public void removeFakeBlock(final @NotNull UUID uuid, final @NotNull Location location) {
        removeFakeBlock(uuid, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public void removeFakeBlock(final @NotNull Player player, final @NotNull Location location) {
        removeFakeBlock(player.getUniqueId(), location);
    }

    private void addFakeBlocks(final @NotNull UUID uuid, final @NotNull Map<BlockPosition, BlockData> blockDataMap) {
        final Map<Vector3i, Material> materials = blockDataMap.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().toVector().toVector3i(),
                e -> e.getValue().getMaterial()
        ));

        getOrCreate(uuid).putAll(materials);
    }

}
