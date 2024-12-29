package pl.auroramc.ui.sdk;

import java.util.Objects;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     * @return the function result
     */
    R apply(T t, U u, V v);

    /**
     * Returns a composed TriFunction that first applies this function to
     * its input, and then applies the after function to the result.
     *
     * @param <W>   the type of output of the after function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the after function
     * @throws NullPointerException if after is null
     */
    default <W> TriFunction<T, U, V, W> andThen(java.util.function.Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
}
