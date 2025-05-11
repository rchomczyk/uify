package dev.shiza.uify.canvas.behaviour;

import dev.shiza.uify.canvas.Canvas;
import org.bukkit.event.inventory.InventoryEvent;

@FunctionalInterface
public interface CanvasGenericBehaviour<T extends Canvas, E extends InventoryEvent> {

    static <T extends Canvas, E extends InventoryEvent> CanvasGenericBehaviour<T, E> undefined() {
        return (state, event) -> {};
    }

    void accept(final CanvasGenericBehaviourState<T> state, final E event);

    default CanvasGenericBehaviour<T, E> andThen(final CanvasGenericBehaviour<T, E> after) {
        if (after == null) {
            throw new NullPointerException("The 'after' CanvasElementBehaviour cannot be null");
        }

        if (this.equals(undefined())) {
            return after;
        }

        return (state, event) -> {
            this.accept(state, event);
            after.accept(state, event);
        };
    }
}
