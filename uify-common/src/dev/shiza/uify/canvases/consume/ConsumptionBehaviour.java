package dev.shiza.uify.canvases.consume;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviourState;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface ConsumptionBehaviour<S extends ConsumptionResultMorph> {

    static CanvasGenericBehaviourState<ConsumingCanvas> typedCopy(final CanvasGenericBehaviourState<Canvas> state) {
        return new CanvasGenericBehaviourState<>(
            state.holder(),
            state.scene(),
            state.canvas().typed(ConsumingCanvas.class));
    }

    void accept(
        final CanvasGenericBehaviourState<ConsumingCanvas> state, final InventoryCloseEvent event, final S result);
}
