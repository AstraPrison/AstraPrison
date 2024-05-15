package dev.fabled.astra.utils.logger;

import org.jetbrains.annotations.NotNull;

public enum AstraLogLevel {

    INFO,
    SUCCESS("<green>"),
    ERROR("<red>");

    @NotNull final String color;

    AstraLogLevel(@NotNull final String color) {
        this.color = color;
    }

    AstraLogLevel() {
        this.color = "";
    }

}
