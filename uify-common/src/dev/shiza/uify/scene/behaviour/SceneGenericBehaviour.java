package dev.shiza.uify.scene.behaviour;

import org.bukkit.event.inventory.InventoryEvent;

@FunctionalInterface
public interface SceneGenericBehaviour<E extends InventoryEvent> {

    void accept(final SceneGenericBehaviourState state, final E event);

    /**
     * Returns a composite behaviour that executes this behaviour first,
     * and then the given behaviour.
     *
     * @param after The behaviour to execute after this behaviour
     * @return Combined behaviour
     * @throws NullPointerException if 'after' is null
     */
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
