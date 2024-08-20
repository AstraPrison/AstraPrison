package dev.fabled.astra.utils.parsers;

@FunctionalInterface
public interface Parser<T> {

    T parse(String s);

}
