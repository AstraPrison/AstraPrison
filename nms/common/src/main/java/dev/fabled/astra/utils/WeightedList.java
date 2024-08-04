package dev.fabled.astra.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedList<E> {

    private final @NotNull NavigableMap<Double, E> map;
    private final @NotNull Random random;
    private double totalWeight;
    private @Nullable E def;

    public WeightedList(final @NotNull Random random) {
        this.map = new TreeMap<>();
        this.random = random;
    }

    public WeightedList(final @NotNull WeightedList<E> list) {
        map = new TreeMap<>(list.map);
        random = list.random;
        totalWeight = list.totalWeight;
        def = list.def;
    }

    public WeightedList() {
        this(new Random());
    }

    public @NotNull WeightedList<E> def(final @Nullable E def) {
        this.def = def;
        return this;
    }

    public @NotNull WeightedList<E> add(final @NotNull E result, final double weight) {
        if (weight <= 0) {
            return this;
        }

        totalWeight += weight;
        map.put(totalWeight, result);
        return this;
    }

    public @Nullable E next() {
        if (map.isEmpty()) {
            return null;
        }

        double value = random.nextDouble() * totalWeight;
        return map.higherEntry(value).getValue();
    }

    public @Nullable E nextOrDef() {
        return map.isEmpty() ? def : next();
    }

    public @Nullable E def() {
        return def;
    }

    public void clear() {
        map.clear();
    }

}
