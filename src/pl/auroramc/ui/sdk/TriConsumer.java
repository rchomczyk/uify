package pl.auroramc.ui.sdk;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    
    void accept(T t, U u, V v);

    /**
     * Returns a composed TriConsumer that performs, in sequence, this
     * operation followed by the after operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an
     * exception, the after operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed TriConsumer that performs in sequence this
     * operation followed by the after operation
     * @throws NullPointerException if after is null
     */
    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after, "The after TriConsumer cannot be null");
        return (t, u, v) -> {
            this.accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}