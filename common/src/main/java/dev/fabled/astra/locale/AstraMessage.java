package dev.fabled.astra.locale;

import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AstraMessage {

    private final @NotNull String key;
    private final @NotNull List<String> message;

    public AstraMessage(final @NotNull YamlConfig config, final @NotNull String key) {
        this.key = key;
        this.message = ListUtils.fromConfig(config, key, Collections.emptyList());
    }

    public @NotNull String getKey() {
        return key;
    }

    public @NotNull List<String> getMessage() {
        return new ArrayList<>(message);
    }

}
