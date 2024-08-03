package dev.fabled.astra.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Version {

    v1_21(766, "v1_21_R1", "1.21"),
    v1_20_6(766, "v1_20_R4", "1.20.5", "1.20.6"),
    v1_20_5(766, "v1_20_R4", "1.20.5", "1.20.6"),
    UNSUPPORTED;

    /**
     * The current server version, or {@link Version#UNSUPPORTED} if not found.
     */
    public static final Version CURRENT;

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

    Version(final int protocolId, final @Nullable String nmsVersion, final @Nullable String... versions) {
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

    public @NotNull Version getLater(@NotNull final Version version) {
        final int comparison = Integer.compare(version.protocolId, protocolId);
        if (comparison > 0) {
            return version;
        }

        return comparison < 0 ? this : version;
    }

    public @NotNull Version getEarlier(@NotNull final Version version) {
        final int comparison = Integer.compare(version.protocolId, protocolId);
        if (comparison < 0) {
            return version;
        }

        return comparison > 0 ? this : version;
    }

}
