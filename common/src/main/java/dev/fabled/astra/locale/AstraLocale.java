package dev.fabled.astra.locale;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AstraLocale {

    private final @NotNull String localeGroup;
    private final @NotNull String prefix;
    private final @NotNull Map<String, AstraMessage> messages;

    public AstraLocale(final @NotNull YamlConfig config, final @NotNull String localeGroup) {
        this.localeGroup = localeGroup;
        messages = new HashMap<>();

        prefix = config.options().getString("prefix", "");
        LocaleManager.getInstance().getMessageKeys().forEach(key ->
                messages.put(key, new AstraMessage(config, key))
        );
    }

    public @NotNull String getLocaleGroup() {
        return localeGroup;
    }

    public @NotNull String getPrefix() {
        return prefix;
    }

    public @NotNull List<String> getMessage(final @NotNull String key) {
        final AstraMessage message = messages.getOrDefault(key, null);
        return message == null ? Collections.emptyList() : message.getMessage();
    }

}
