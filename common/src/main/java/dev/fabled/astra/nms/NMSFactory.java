package dev.fabled.astra.nms;

import dev.fabled.astra.utils.Version;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NMSFactory {

    private static @Nullable AbstractNMSHandler nmsHandler;

    public static boolean onEnable() {
        if (nmsHandler != null) {
            return true;
        }

        final Version version = Version.CURRENT;
        if (version == Version.UNSUPPORTED) {
            return false;
        }

        try {
            nmsHandler = (AbstractNMSHandler) Class.forName(
                    "dev.fabled.astra.nms.versions." + version.getNMSVersion() + ".NMSHandler"
            )
                    .getConstructor()
                    .newInstance();
        }
        catch (Exception e) {
            AstraLog.log(e);
            return false;
        }

        return true;
    }

    public static AbstractNMSHandler getNMSHandler() {
        if (nmsHandler == null) {
            throw new RuntimeException("The NMS handler has not been initialized yet, or you are on an unsupported version!");
        }

        return nmsHandler;
    }

}
