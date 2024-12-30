package dev.shiza.uify.initialization;

import java.util.function.Supplier;

public final class Lazy<T> {

    private final Supplier<T> valueInitializer;
    private T value;

    private Lazy(final Supplier<T> valueInitializer) {
        this.valueInitializer = valueInitializer;
    }

    public static <T> Lazy<T> lazy(final Supplier<T> valueInitializer) {
        return new Lazy<>(valueInitializer);
    }

    public T get() {
        if (value == null) {
            value = valueInitializer.get();
        }

        return value;
    }
}