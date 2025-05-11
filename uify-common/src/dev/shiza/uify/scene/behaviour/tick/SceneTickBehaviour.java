package dev.shiza.uify.scene.behaviour.tick;

import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

@FunctionalInterface
public interface SceneTickBehaviour {

    static SceneTickBehaviour undefined() {
        return holder -> {};
    }

    void accept(final SceneInventoryHolder holder);

    default SceneTickBehaviour andThen(final SceneTickBehaviour after) {
        if (after == null) {
            throw new NullPointerException("The 'after' SceneTickBehaviour cannot be null");
        }

        if (this.equals(undefined())) {
            return after;
        }

        return holder -> {
            this.accept(holder);
            after.accept(holder);
        };
    }
}