package dev.shiza.uify.scene.behaviour;

import org.bukkit.event.inventory.InventoryEvent;

@FunctionalInterface
public interface SceneGenericBehaviour<E extends InventoryEvent> {

    static <E extends InventoryEvent> SceneGenericBehaviour<E> undefined() {
        return (state, event) -> {};
    }

    void accept(final SceneGenericBehaviourState state, final E event);

    default SceneGenericBehaviour<E> andThen(final SceneGenericBehaviour<E> after) {
        if (after == null) {
            throw new NullPointerException("The 'after' CanvasElementBehaviour cannot be null");
        }

        return (state, event) -> {
            this.accept(state, event);
            after.accept(state, event);
        };
    }
}
