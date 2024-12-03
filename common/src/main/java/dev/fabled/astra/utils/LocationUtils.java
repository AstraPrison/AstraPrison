package dev.fabled.astra.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class LocationUtils {

    public static final char DELIMITER;

    static {
        DELIMITER = ';';
    }

    @Contract("_, !null -> !null")
    public static @Nullable String serialize(final @Nullable Location location, final @Nullable String def) {
        if (location == null) {
            return def;
        }

        return location.getWorld().getUID().toString() + DELIMITER
                + location.getX() + DELIMITER
                + location.getY() + DELIMITER
                + location.getZ();
    }

    public static @Nullable String serialize(final @Nullable Location location) {
        return serialize(location, null);
    }

    @Contract("_, !null -> !null")
    public static @Nullable String serializeBlock(final @Nullable Location location, final @Nullable String def) {
        if (location == null) {
            return def;
        }

        return location.getWorld().getUID().toString() + DELIMITER
                + location.getBlockX() + DELIMITER
                + location.getBlockY() + DELIMITER
                + location.getBlockZ();
    }

    public static @Nullable String serializeBlock(final @Nullable Location location) {
        return serializeBlock(location, null);
    }

    public static @Nullable Location deserialize(final @Nullable String input) {
        if (input == null) {
            return null;
        }

        final String[] split = input.split(String.valueOf(DELIMITER));
        if (split.length < 4) {
            return null;
        }

        final UUID uuid;
        double x, y, z;
        try {
            uuid = UUID.fromString(split[0]);
            x = Double.parseDouble(split[1]);
            y = Double.parseDouble(split[2]);
            z = Double.parseDouble(split[3]);
        }
        catch (IllegalArgumentException e) {
            return null;
        }

        final World world = Bukkit.getWorld(uuid);
        if (world == null) {
            return null;
        }

        return new Location(world, x, y, z);
    }

}
