package dev.paracausal.astra.logger;

public enum AstraLogLevel {

    INFO(""), SUCCESS("<green>"), ERROR("<red>");

    final String color;

    AstraLogLevel(final String color) {
        this.color = color;
    }

}
