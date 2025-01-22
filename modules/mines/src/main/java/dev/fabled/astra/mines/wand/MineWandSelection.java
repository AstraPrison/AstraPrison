package dev.fabled.astra.mines.wand;

import dev.fabled.astra.modules.impl.MinesModule;
import dev.fabled.astra.permissions.AstraPermission;
import dev.fabled.astra.utils.LocationUtils;
import dev.fabled.astra.utils.configuration.JsonConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MineWandSelection {

    private final @NotNull Map<UUID, Location> cornerOne;
    private final @NotNull Map<UUID, Location> cornerTwo;

    public MineWandSelection() {
        cornerOne = new HashMap<>();
        cornerTwo = new HashMap<>();
    }

    public boolean hasCornerOne(final @NotNull UUID uuid) {
        return cornerOne.containsKey(uuid);
    }

    public boolean hasCornerOne(final @NotNull Player player) {
        return hasCornerOne(player.getUniqueId());
    }

    public boolean hasCornerTwo(final @NotNull UUID uuid) {
        return cornerTwo.containsKey(uuid);
    }

    public boolean hasCornerTwo(final @NotNull Player player) {
        return hasCornerTwo(player.getUniqueId());
    }

    /**
     * Checks if the given user has a valid selection!
     * @param uuid The player's {@link UUID}
     * @return
     * <br><code>0</code> if they have a valid selection
     * <br><code>1</code> if they are missing corner one
     * <br><code>2</code> if they are missing corner two
     * <br><code>3</code> if the corners are not in the same world
     */
    public int hasValidSelection(final @NotNull UUID uuid) {
        if (!hasCornerOne(uuid)) {
            return 1;
        }

        if (!hasCornerTwo(uuid)) {
            return 2;
        }

        final Location one = cornerOne.get(uuid);
        final Location two = cornerTwo.get(uuid);
        if (one.getWorld().getUID().compareTo(two.getWorld().getUID()) != 0) {
            return 3;
        }

        return 0;
    }

    /**
     * Checks if the given user has a valid selection!
     * @param player The {@link Player}
     * @return
     * <br><code>0</code> if they have a valid selection
     * <br><code>1</code> if they are missing corner one
     * <br><code>2</code> if they are missing corner two
     * <br><code>3</code> if the corners are not in the same world
     * @see MineWandSelection#hasValidSelection(UUID)
     */
    public int hasValidSelection(final @NotNull Player player) {
        return hasValidSelection(player.getUniqueId());
    }

    /**
     * Update the player's corner one position!
     * @param uuid The player's {@link UUID}
     * @param location The {@link Location} for corner one
     * @return
     * <br><code>true</code> if the new location is different from the previous selection or if they had no previous selection
     * <br><code>false</code> if the new location is the same as the previous selection
     */
    public boolean setCornerOne(final @NotNull UUID uuid, final @NotNull Location location) {
        return setCorner(uuid, location, cornerOne);
    }

    /**
     * Update the player's corner one position!
     * @param player The {@link Player}
     * @param location The {@link Location} for corner one
     * @return
     * <br><code>true</code> if the new location is different from the previous selection or if they had no previous selection
     * <br><code>false</code> if the new location is the same as the previous selection
     */
    public boolean setCornerOne(final @NotNull Player player, final @NotNull Location location) {
        return setCorner(player.getUniqueId(), location, cornerOne);
    }

    /**
     * Update the player's corner two position!
     * @param uuid The player's {@link UUID}
     * @param location The {@link Location} for corner two
     * @return
     * <br><code>true</code> if the new location is different from the previous selection or if they had no previous selection
     * <br><code>false</code> if the new location is the same as the previous selection
     */
    public boolean setCornerTwo(final @NotNull UUID uuid, final @NotNull Location location) {
        return setCorner(uuid, location, cornerTwo);
    }

    /**
     * Update the player's corner two position!
     * @param player The {@link Player}
     * @param location The {@link Location} for corner two
     * @return
     * <br><code>true</code> if the new location is different from the previous selection or if they had no previous selection
     * <br><code>false</code> if the new location is the same as the previous selection
     */
    public boolean setCornerTwo(final @NotNull Player player, final @NotNull Location location) {
        return setCorner(player.getUniqueId(), location, cornerTwo);
    }

    private boolean setCorner(final @NotNull UUID uuid, final @NotNull Location location, final @NotNull Map<UUID, Location> map) {
        final Location current = map.getOrDefault(uuid, null);
        map.put(uuid, location);

        if (current == null) {
            return true;
        }

        if (current.getWorld().getUID().compareTo(location.getWorld().getUID()) != 0) {
            return true;
        }

        return current.getBlockX() != location.getBlockX() ||
                current.getBlockY() != location.getBlockY() ||
                current.getBlockZ() != location.getBlockZ();
    }

    public @Nullable Location getCornerOne(final @NotNull UUID uuid) {
        return cornerOne.getOrDefault(uuid, null);
    }

    public @Nullable Location getCornerOne(final @NotNull Player player) {
        return getCornerOne(player.getUniqueId());
    }

    public @Nullable Location getCornerTwo(final @NotNull UUID uuid) {
        return cornerTwo.getOrDefault(uuid, null);
    }

    public @Nullable Location getCornerTwo(final @NotNull Player player) {
        return getCornerTwo(player.getUniqueId());
    }

    public void clearSelection(final @NotNull UUID uuid) {
        cornerOne.remove(uuid);
        cornerTwo.remove(uuid);
    }

    public void clearSelection(final @NotNull Player player) {
        clearSelection(player.getUniqueId());
    }

    public void loadPlayer(final @NotNull UUID uuid) {
        final JsonConfig config = MinesModule.getInstance().getClipboardJson();
        final String path = "players." + uuid + ".";

        final String cornerOneSerialized = config.options().getString(path + "corner-one", null);
        final Location cornerOne = LocationUtils.deserialize(cornerOneSerialized);
        if (cornerOne != null) {
            setCornerOne(uuid, cornerOne);
        }

        final String cornerTwoSerialized = config.options().getString(path + "corner-two", null);
        final Location cornerTwo = LocationUtils.deserialize(cornerTwoSerialized);
        if (cornerTwo != null) {
            setCornerTwo(uuid, cornerTwo);
        }
    }

    public void loadPlayer(final @NotNull Player player) {
        if (removeFromFileIfNoPermission(player)) {
            return;
        }

        loadPlayer(player.getUniqueId());
    }

    public void savePlayer(final @NotNull UUID uuid) {
        final JsonConfig config = MinesModule.getInstance().getClipboardJson();
        final String path = "players." + uuid + ".";

        final String cornerOneSerialized = LocationUtils.serializeBlock(getCornerOne(uuid));
        if (cornerOneSerialized != null) {
            config.options().set(path + "corner-one", cornerOneSerialized);
        }

        final String cornerTwoSerialized = LocationUtils.serializeBlock(getCornerTwo(uuid));
        if (cornerTwoSerialized != null) {
            config.options().set(path + "corner-two", cornerTwoSerialized);
        }

        config.saveChanges();
        config.reload();
    }

    public void savePlayer(final @NotNull Player player) {
        savePlayer(player.getUniqueId());
    }

    public boolean removeFromFileIfNoPermission(final @NotNull Player player) {
        if (AstraPermission.MINE_WAND_USE.hasPermission(player)) {
            return false;
        }

        final JsonConfig config = MinesModule.getInstance().getClipboardJson();
        final String path = "players." + player.getUniqueId() + ".";
        boolean save = false;

        if (config.options().has(path + "corner-one")) {
            config.options().set(path + "corner-one", null);
            save = true;
        }

        if (config.options().has(path + "corner-two")) {
            config.options().set(path + "corner-two", null);
            save = true;
        }

        if (!save) {
            return true;
        }

        config.saveChanges();
        config.reload();
        return true;
    }

    public void saveAll() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (removeFromFileIfNoPermission(player)) {
                return;
            }

            savePlayer(player);
        });
    }

}
