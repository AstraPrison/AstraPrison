package dev.fabled.astra.omnitool.utils;

import java.util.List;

public class EnchantmentData {
    private final String name;
    private final boolean enabled;
    private final String type;
    private final String eventTrigger;
    private final double maxChance;
    private final int maxLevel;
    private final String currency;
    private final int startingCost;
    private final int increaseCostBy;
    private final int startingLevel;
    private final List<String> actions;

    public EnchantmentData(String name, boolean enabled, String type, String eventTrigger, double maxChance, int maxLevel,
                           String currency, int startingCost, int increaseCostBy, int startingLevel, List<String> actions) {
        this.name = name;
        this.enabled = enabled;
        this.type = type;
        this.eventTrigger = eventTrigger;
        this.maxChance = maxChance;
        this.maxLevel = maxLevel;
        this.currency = currency;
        this.startingCost = startingCost;
        this.increaseCostBy = increaseCostBy;
        this.startingLevel = startingLevel;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getType() {
        return type;
    }

    public String getEventTrigger() {
        return eventTrigger;
    }

    public double getMaxChance() {
        return maxChance;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getCurrency() {
        return currency;
    }

    public int getStartingCost() {
        return startingCost;
    }

    public int getIncreaseCostBy() {
        return increaseCostBy;
    }

    public int getStartingLevel() {
        return startingLevel;
    }

    public List<String> getActions() {
        return actions;
    }
}