package dev.shiza.uify.canvases.sequential;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.canvas.behaviour.tick.CanvasTickBehaviour;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface SequentialCanvas extends Canvas {

    static SequentialCanvas rows(final int rows) {
        return new SequentialCanvasImpl(new ArrayList<>())
            .position(position -> position.minimum(0, 0).maximum(rows - 1, 8));
    }

    default SequentialCanvas elements(final Collection<? extends CanvasElement> elements) {
        return elements(elements, false);
    }

    default SequentialCanvas onSequentialCanvasTick(final CanvasTickBehaviour<SequentialCanvas> canvasTickBehaviour) {
        onCanvasTick(SequentialCanvas.class, canvasTickBehaviour);
        return this;
    }

    SequentialCanvas elements(final CanvasElement... elements);

    SequentialCanvas elements(final Collection<? extends CanvasElement> elements, final boolean override);

    @Override
    SequentialCanvas position(final UnaryOperator<CanvasPosition> mutator);

    @Override
    SequentialCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);
}
