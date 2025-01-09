package dev.shiza.uify.canvas.element.behaviour;

import dev.shiza.uify.canvas.Canvas;
import org.bukkit.event.inventory.InventoryInteractEvent;

@FunctionalInterface
public interface CanvasElementBehaviour<T extends Canvas, E extends InventoryInteractEvent> {

    void accept(final CanvasElementBehaviourState<T> state, final E event);

    default CanvasElementBehaviour<T, E> andThen(CanvasElementBehaviour<T, E> after) {
        if (after == null) {
            throw new NullPointerException("The 'after' CanvasElementBehaviour cannot be null");
        }

        return (state, event) -> {
            this.accept(state, event);
            after.accept(state, event);
        };
    }
}
