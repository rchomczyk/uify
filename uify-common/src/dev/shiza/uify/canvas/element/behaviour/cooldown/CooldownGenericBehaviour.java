package dev.shiza.uify.canvas.element.behaviour.cooldown;

import dev.shiza.uify.canvas.Canvas;
import org.bukkit.event.inventory.InventoryInteractEvent;

@FunctionalInterface
public interface CooldownGenericBehaviour<T extends Canvas, E extends InventoryInteractEvent> {

    static <T extends Canvas, E extends InventoryInteractEvent> CooldownGenericBehaviour<T, E> undefined() {
        return (state, event) -> {};
    }

    void accept(final CooldownGenericBehaviourState<T> state, final E event);

    default CooldownGenericBehaviour<T, E> andThen(final CooldownGenericBehaviour<T, E> after) {
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
