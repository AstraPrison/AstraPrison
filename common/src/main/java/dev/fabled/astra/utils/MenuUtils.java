package dev.fabled.astra.utils;

public class MenuUtils {

    public static int convertToMenuSize(int input) {
        input = Math.max(9, input);
        input = Math.min(54, input);
        return input - (input % 9);
    }

}
