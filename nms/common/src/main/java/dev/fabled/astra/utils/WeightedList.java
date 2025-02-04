package dev.fabled.astra.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public final class WeightedList<E> {

    public static <E> @NotNull WeightedList<E> of(final @NotNull List<E> elements) {
        return new WeightedList<E>().add(elements);
    }

    @SafeVarargs
    public static <E> @NotNull WeightedList<E> of(final @NotNull E... elements) {
        return new WeightedList<E>().add(elements);
    }

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

    public @NotNull WeightedList<E> add(final @NotNull List<E> elements) {
        for (final E element : elements) {
            totalWeight += 100.0d;
            map.put(totalWeight, element);
        }

        return this;
    }

    @SafeVarargs
    public final @NotNull WeightedList<E> add(final @NotNull E... elements) {
        return add(List.of(elements));
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

    public void forEach(final BiConsumer<Double, E> action) {
        Objects.requireNonNull(action);
        for (final Map.Entry<Double, E> entry : map.entrySet()) {
            double key;
            E value;
            try {
                key = entry.getKey();
                value = entry.getValue();
            }
            catch (IllegalStateException e) {
                throw new ConcurrentModificationException(e);
            }
            action.accept(key, value);
        }
    }

}
