package dev.shiza.uify.canvas.tick;

import dev.shiza.uify.canvas.Canvas;

@FunctionalInterface
public interface CanvasTickBehaviour<T extends Canvas> {

    static <T extends Canvas> CanvasTickBehaviour<T> undefined() {
        return state -> {};
    }

    void accept(final CanvasTickBehaviourState<T> state);

    default CanvasTickBehaviour<T> andThen(final CanvasTickBehaviour<T> after) {
        if (after == null) {
            throw new NullPointerException("The 'after' CanvasTickBehaviour cannot be null");
        }

        return holder -> {
            this.accept(holder);
            after.accept(holder);
        };
    }
}