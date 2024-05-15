package dev.fabled.astra.lang;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class LocaleGroup {

    private record Message(@NotNull List<String> message) {}

    @NotNull final String id;
    @NotNull final Map<String, Message> messages;

    LocaleGroup(@NotNull final String id, @NotNull final YamlConfig config) {
        this.id = id;
        this.messages = new HashMap<>();

        Astra.getUtilities().getLocaleManager().getKeys().forEach(string -> {
            messages.put(string, new Message(ListUtils.fromConfig(config, string)));
        });
    }

    @NotNull List<String> getMessage(@NotNull final String key) {
        final Message m = messages.getOrDefault(key, null);
        if (m == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(m.message);
    }

}
