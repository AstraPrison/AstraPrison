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

    private final @NotNull MiniMessage miniMessage;

    MiniColor(final @NotNull MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    MiniColor(final @NotNull TagResolver... tagResolvers) {
        miniMessage = MiniMessage.builder().tags(TagResolver.builder().resolvers(tagResolvers).build()).build();
    }

    public @NotNull Component deserialize(final @NotNull String input) {
        if (this == INVENTORY) {
            return miniMessage.deserialize(input).decoration(TextDecoration.ITALIC, false);
        }

        return miniMessage.deserialize(input);
    }

    public @NotNull List<Component> deserialize(final @NotNull List<String> input) {
        final List<Component> list = new ArrayList<>();
        if (this == INVENTORY) {
            input.forEach(string -> list.add(miniMessage.deserialize(string).decoration(TextDecoration.ITALIC, false)));
        }

        else {
            input.forEach(string -> list.add(miniMessage.deserialize(string)));
        }

        return list;
    }

    public @NotNull String serialize(final @NotNull Component input) {
        return miniMessage.serialize(input);
    }

    public @NotNull List<String> serialize(final @NotNull List<Component> input) {
        final List<String> list = new ArrayList<>();
        input.forEach(string -> list.add(serialize(string)));
        return list;
    }

    public @NotNull String stripTags(final @NotNull String input) {
        return miniMessage.stripTags(input);
    }

    public @NotNull List<String> stripTags(final @NotNull List<String> input) {
        final List<String> list = new ArrayList<>();
        input.forEach(string -> list.add(stripTags(string)));
        return list;
    }

}
