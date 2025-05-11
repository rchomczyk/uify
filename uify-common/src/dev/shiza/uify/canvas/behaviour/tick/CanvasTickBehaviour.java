package dev.shiza.uify.canvas.behaviour.tick;

import dev.shiza.uify.canvas.Canvas;
import java.time.Duration;

@FunctionalInterface
public interface CanvasTickBehaviour<T extends Canvas> {

    static <T extends Canvas> CanvasTickBehaviour<T> undefined() {
        return state -> Duration.ofMillis(-1);
    }

    Duration accept(final CanvasTickBehaviourState<T> state);

    default CanvasTickBehaviour<T> andThen(final CanvasTickBehaviour<T> after) {
        if (after == null) {
            throw new NullPointerException("The 'after' CanvasTickBehaviour cannot be null");
        }

        if (this.equals(undefined())) {
            return after;
        }

        return holder -> {
            final Duration initialDelay = this.accept(holder);
            final Duration afterDelay = after.accept(holder);
            return initialDelay.compareTo(afterDelay) < 0 ? initialDelay : afterDelay;
        };
    }
}