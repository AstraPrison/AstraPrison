package dev.fabled.astra.lang;

import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class LocaleGroup {

    record Message(@NotNull List<String> message) {}

    final @Nullable String prefix;
    final @NotNull Map<String, Message> messages;

    LocaleGroup(@NotNull final YamlConfig config) {
        prefix = config.options().getString("prefix", null);
        messages = new HashMap<>();

        for (final String key : LocaleManager.getInstance().messageKeys) {
            if (!config.options().contains(key)) {
                continue;
            }

            messages.put(key, new Message(ListUtils.fromConfig(config, key)));
        }
    }

    @NotNull String getPrefix() {
        if (prefix == null) {
            final LocaleGroup defGroup = LocaleManager.getInstance().getDefaultLocaleGroup();
            if (defGroup == this) {
                return "";
            }

            return defGroup.getPrefix();
        }

        return prefix;
    }

    @NotNull List<String> getMessage(@NotNull final String key) {
        final Message message = messages.getOrDefault(key, null);
        if (message == null) {
            final LocaleGroup defGroup = LocaleManager.getInstance().getDefaultLocaleGroup();
            if (defGroup == this) {
                return new ArrayList<>();
            }

            return defGroup.getMessage(key);
        }

        return new ArrayList<>(message.message);
    }

}
