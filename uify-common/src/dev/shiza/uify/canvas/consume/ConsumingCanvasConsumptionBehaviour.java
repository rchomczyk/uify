package dev.shiza.uify.canvas.consume;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.CanvasTypingException;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviourState;
import java.util.List;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;

public interface ConsumingCanvasConsumptionBehaviour<E extends InventoryEvent> {

    static CanvasGenericBehaviourState<ConsumingCanvas> typedCopy(final CanvasGenericBehaviourState<Canvas> state) {
        if (!(state.canvas() instanceof ConsumingCanvas canvas)) {
            throw new CanvasTypingException(
                "Could not create an copy of generic behaviour state, due to invalid type.");
        }

        return new CanvasGenericBehaviourState<>(state.holder(), state.scene(), canvas);
    }

    void accept(
        final CanvasGenericBehaviourState<ConsumingCanvas> state, final E event, final List<ItemStack> consumed);
}
