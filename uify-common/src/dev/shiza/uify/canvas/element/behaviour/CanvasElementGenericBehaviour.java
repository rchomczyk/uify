package dev.shiza.uify.canvas.element.behaviour;

import dev.shiza.uify.canvas.Canvas;
import org.bukkit.event.inventory.InventoryInteractEvent;

@FunctionalInterface
public interface CanvasElementGenericBehaviour<T extends Canvas, E extends InventoryInteractEvent> {

    static <T extends Canvas, E extends InventoryInteractEvent> CanvasElementGenericBehaviour<T, E> undefined() {
        return (state, event) -> {};
    }

    void accept(final CanvasElementGenericBehaviourState<T> state, final E event);

    default CanvasElementGenericBehaviour<T, E> andThen(final CanvasElementGenericBehaviour<T, E> after) {
        if (after == null) {
            throw new NullPointerException("The 'after' CanvasElementBehaviour cannot be null");
        }

        return (state, event) -> {
            this.accept(state, event);
            after.accept(state, event);
        };
    }
}
