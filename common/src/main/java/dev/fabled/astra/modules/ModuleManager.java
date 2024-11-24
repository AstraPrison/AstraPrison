package dev.fabled.astra.modules;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ModuleManager {

    private final @NotNull Map<String, AstraModule> modules;

    public ModuleManager() {
        modules = new HashMap<>();
    }

    public void register(final @NotNull AstraModule module) {
        modules.putIfAbsent(module.getId(), module);
    }

    public void onLoad() {
        modules.forEach((id, module) -> module.onLoad());
    }

    public void onEnable() {
        modules.forEach((id, module) -> module.onEnable());
    }

    public void onDisable() {
        modules.forEach((id, module) -> module.onDisable());
    }

    public void onReload() {
        modules.forEach((id, module) -> module.onReload());
    }

}
