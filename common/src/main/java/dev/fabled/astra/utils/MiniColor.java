package dev.fabled.astra.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum MiniColor {

    CHAT(MiniMessage.miniMessage()),
    INVENTORY(
            StandardTags.color(),
            StandardTags.gradient(),
            StandardTags.rainbow(),
            StandardTags.decorations(),
            StandardTags.font(),
            StandardTags.reset()
    ),
    CONSOLE(
            StandardTags.color(),
            StandardTags.reset()
    );

    private @NotNull final MiniMessage miniMessage;

    MiniColor(@NotNull final MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    MiniColor(@NotNull final TagResolver... tagResolvers) {
        miniMessage = MiniMessage.builder().tags(TagResolver.builder().resolvers(tagResolvers).build()).build();
    }

    public static @NotNull Component parse(@NotNull final String string) {
        return CHAT.miniMessage.deserialize(string);
    }

    public @NotNull Component deserialize(@NotNull final String string) {
        return miniMessage.deserialize(string).decoration(TextDecoration.ITALIC, false);
    }

    public @NotNull List<Component> deserialize(@NotNull final List<String> strings) {
        final List<Component> components = new ArrayList<>();

        strings.forEach(string -> components.add(
                miniMessage.deserialize(string).decoration(TextDecoration.ITALIC, false)
        ));

        return components;
    }

    public @NotNull String serialize(@NotNull final Component component) {
        return miniMessage.serialize(component);
    }

    public @NotNull List<String> serialize(@NotNull final List<Component> components) {
        final List<String> strings = new ArrayList<>();
        components.forEach(component -> strings.add(miniMessage.serialize(component)));
        return strings;
    }

    public @NotNull String stripTags(@NotNull final String string) {
        return miniMessage.stripTags(string);
    }

    public @NotNull List<String> stripTags(@NotNull final List<String> strings) {
        final List<String> parsed = new ArrayList<>();
        strings.forEach(string -> parsed.add(miniMessage.stripTags(string)));
        return parsed;
    }

}
