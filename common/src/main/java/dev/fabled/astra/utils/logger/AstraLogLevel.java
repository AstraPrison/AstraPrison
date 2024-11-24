package dev.fabled.astra.utils.logger;

import org.jetbrains.annotations.NotNull;

public enum AstraLogLevel {

    INFO,
    SUCCESS("<green>"),
    ERROR("<red>");

    final @NotNull String color;

    AstraLogLevel(final @NotNull String color) {
        this.color = color;
    }

    AstraLogLevel() {
        this("");
    }

}
