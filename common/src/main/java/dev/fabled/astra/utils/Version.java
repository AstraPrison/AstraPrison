package dev.fabled.astra.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Version {

    v1_21_4(769, "v1_21_R3", "1.21.4"),
    v1_21_2(768, "v1_21_R2", "1.21.2", "1.21.3"),
    v1_21(767, "v1_21_R1", "1.21", "1.21.1"),
    UNSUPPORTED;

    /**
     * The current server version, or {@link Version#UNSUPPORTED}!
     */
    public static final @NotNull Version CURRENT;

    static {
        final String gameVersion = Bukkit.getBukkitVersion().split("-")[0];

        Version v = UNSUPPORTED;
        for (final Version version : Version.values()) {
            if (version.gameVersions.contains(gameVersion)) {
                v = version;
                break;
            }
        }

        CURRENT = v;
    }

    private final int protocolId;
    private final @Nullable String nmsVersion;
    private final @NotNull List<String> gameVersions;

    Version(final int protocolId, final @Nullable String nmsVersion, final @NotNull String... versions) {
        this.protocolId = protocolId;
        this.nmsVersion = nmsVersion;
        this.gameVersions = List.of(versions);
    }

    Version() {
        protocolId = -1;
        nmsVersion = null;
        gameVersions = Collections.emptyList();
    }

    public @Nullable String getNMSVersion() {
        return nmsVersion;
    }

    public @NotNull List<String> getGameVersions() {
        return new ArrayList<>(gameVersions);
    }

    public @NotNull Version getLater(final @NotNull Version version) {
        final int comparison = Integer.compare(version.protocolId, this.protocolId);

        // If version is later
        if (comparison > 0) {
            return version;
        }

        // If version is earlier
        if (comparison < 0) {
            return this;
        }

        // If they are the same
        return version;
    }

    public @NotNull Version getEarlier(final @NotNull Version version) {
        final int comparison = Integer.compare(version.protocolId, this.protocolId);

        // If version is earlier
        if (comparison < 0) {
            return version;
        }

        // If version is later
        if (comparison > 0) {
            return this;
        }

        // If they are the same
        return version;
    }

}
