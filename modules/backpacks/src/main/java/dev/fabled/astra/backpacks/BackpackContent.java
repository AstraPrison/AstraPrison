package dev.fabled.astra.backpacks;

public class BackpackContent {

    private int amount;

    public BackpackContent(final int amount) {
        this.amount = amount;
    }

    public BackpackContent() {
        this(0);
    }

    public void add(final int amount) {
        this.amount += amount;
    }

    public int remove(final int amount) {
        this.amount -= amount;
        return amount;
    }

    public int get() {
        return amount;
    }

}
