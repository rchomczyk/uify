package dev.shiza.uify.canvas.layout;

import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface LayoutCanvas extends Canvas {

    static LayoutCanvas pattern(final String pattern) {
        return new LayoutCanvasImpl(pattern, new HashMap<>(), new HashMap<>());
    }

    static LayoutCanvas pattern(final String... patterns) {
        return new LayoutCanvasImpl(String.join("\n", patterns), new HashMap<>(), new HashMap<>());
    }

    LayoutCanvas bind(final int row, final int column, final CanvasElement element);

    LayoutCanvas bind(final char source, final CanvasElement element);

    @Override
    LayoutCanvas position(final UnaryOperator<CanvasPosition> mutator);

    @Override
    LayoutCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);
}
