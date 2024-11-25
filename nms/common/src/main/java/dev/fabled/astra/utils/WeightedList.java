package dev.fabled.astra.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public final class WeightedList<E> {

    private final @NotNull NavigableMap<Double, E> map;
    private final @NotNull Random random;
    private double totalWeight;

    public WeightedList() {
        map = new TreeMap<>();
        random = new Random();
    }

    public WeightedList(final @NotNull Random random) {
        map = new TreeMap<>();
        this.random = random;
    }

    public WeightedList(final @NotNull WeightedList<E> list) {
        map = new TreeMap<>(list.map);
        random = list.random;
        totalWeight = list.totalWeight;
    }

    public @NotNull WeightedList<E> add(final @NotNull E element, final double weight) {
        if (weight <= 0) {
            return this;
        }

        totalWeight += weight;
        map.put(totalWeight, element);
        return this;
    }

    public @Nullable E next() {
        if (map.isEmpty()) {
            return null;
        }

        double value = random.nextDouble() * totalWeight;
        return map.higherEntry(value).getValue();
    }

    @Contract("!null -> !null")
    public @Nullable E nextOrDef(final @Nullable E def) {
        return map.isEmpty() ? def : next();
    }

    public void clear() {
        map.clear();
        totalWeight = 0;
    }

}
