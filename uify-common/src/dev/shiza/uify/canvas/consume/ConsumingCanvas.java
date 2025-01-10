package dev.shiza.uify.canvas.consume;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public interface ConsumingCanvas extends Canvas {

    static ConsumingCanvas rows(final int rows) {
        return new ConsumingCanvasImpl(new ArrayList<>())
            .position(
                position -> position
                    .minimum(0, 0)
                    .maximum(rows - 1, 8));
    }

    ConsumingCanvas populateItems(final Collection<ItemStack> items);

    ConsumingCanvas populateItems(final Collection<ItemStack> items, final boolean override);

    ConsumingCanvas populate(final Collection<? extends CanvasElement> elements);

    ConsumingCanvas populate(final Collection<? extends CanvasElement> elements, final boolean override);

    @Override
    ConsumingCanvas position(final UnaryOperator<CanvasPosition> mutator);

    @Override
    ConsumingCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);

    List<ItemStack> consume(final SceneInventoryHolder holder);
}
