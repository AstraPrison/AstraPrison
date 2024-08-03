package dev.fabled.astra.nms;

import dev.fabled.astra.utils.Version;
import dev.fabled.astra.utils.logger.AstraLog;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class NMSFactory {

    private static AbstractNMSHandler HANDLER;

    public static boolean onEnable(final @NotNull JavaPlugin plugin) {
        if (HANDLER != null) {
            return true;
        }

        final Version version = Version.CURRENT;
        if (version == Version.UNSUPPORTED) {
            return false;
        }

        try {
            HANDLER = (AbstractNMSHandler) Class.forName("dev.fabled.astra.nms.versions." + version.getNMSVersion() + ".NMSHandler")
                    .getConstructor(JavaPlugin.class)
                    .newInstance(plugin);
        }
        catch (Exception e) {
            AstraLog.log(e);
            return false;
        }

        return true;
    }

    public static AbstractNMSHandler getHandler() {
        if (HANDLER == null) {
            throw new RuntimeException("The NMS handler has not been initialized or you're on an unsupported version!");
        }

        return HANDLER;
    }

}
