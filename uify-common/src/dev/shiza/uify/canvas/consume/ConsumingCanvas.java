package dev.shiza.uify.canvas.consume;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public interface ConsumingCanvas extends Canvas, ConsumingCanvasBehaviours {

    static ConsumingCanvas rows(final int rows) {
        return new ConsumingCanvasImpl(new ArrayList<>(), new HashMap<>())
            .position(
                position -> position
                    .minimum(0, 0)
                    .maximum(rows - 1, 8));
    }

    default ConsumingCanvas populateItems(final Collection<ItemStack> items) {
        return populateItems(items, false);
    }

    default ConsumingCanvas populateItems(final Map<Position, ? extends ItemStack> elements) {
        return populateItems(elements, false);
    }

    default ConsumingCanvas populate(final Collection<? extends CanvasElement> elements) {
        return populate(elements, false);
    }

    default ConsumingCanvas populate(final Map<Position, ? extends CanvasElement> elements) {
        return populate(elements, false);
    }

    ConsumingCanvas populateItems(final Collection<ItemStack> items, final boolean override);

    ConsumingCanvas populateItems(final Map<Position, ? extends ItemStack> elements, final boolean override);

    ConsumingCanvas populate(final Collection<? extends CanvasElement> elements, final boolean override);

    ConsumingCanvas populate(final Map<Position, ? extends CanvasElement> elements, final boolean override);

    @Override
    ConsumingCanvas position(final UnaryOperator<CanvasPosition> mutator);

    @Override
    ConsumingCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);

    ConsumptionResult consume(final SceneInventoryHolder holder);

    IndexedConsumptionResult consumeWithIndexes(final SceneInventoryHolder holder);
}
