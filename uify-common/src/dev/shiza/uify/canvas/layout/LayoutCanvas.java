package dev.shiza.uify.canvas.layout;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.CanvasTypingException;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.canvas.tick.CanvasTickBehaviour;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface LayoutCanvas extends Canvas {

    static LayoutCanvas pattern(final String pattern) {
        return new LayoutCanvasImpl(pattern, new HashMap<>(), new HashMap<>());
    }

    static LayoutCanvas pattern(final String... patterns) {
        return new LayoutCanvasImpl(String.join("\n", patterns), new HashMap<>(), new HashMap<>());
    }

    static LayoutCanvas border(final char source, final int rows, final int columns, final CanvasElement element) {
        if (rows < 3 || columns < 3) {
            throw new CanvasTypingException(
                "Layout canvas requires at least three rows and three columns to be able to create border.");
        }

        final StringBuilder patternBuilder = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            final boolean isTopBorder = row == 0;
            final boolean isBottomBorder = row == rows - 1;
            final boolean isAnyOfBorders = isTopBorder || isBottomBorder;
            if (isAnyOfBorders) {
                patternBuilder.append(String.valueOf(source).repeat(columns));
            } else {
                final int contentLength = columns - 2;
                patternBuilder.append(source).append(" ".repeat(contentLength)).append(source);
            }
            patternBuilder.append("\n");
        }

        return pattern(patternBuilder.toString()).bind(source, element);
    }

    static LayoutCanvas border(final int rows, final int columns, final CanvasElement element) {
        return border('x', rows, columns, element);
    }

    default LayoutCanvas onLayoutCanvasTick(final CanvasTickBehaviour<LayoutCanvas> canvasTickBehaviour) {
        onCanvasTick(LayoutCanvas.class, canvasTickBehaviour);
        return this;
    }

    LayoutCanvas bind(final int row, final int column, final CanvasElement element);

    LayoutCanvas bind(final char source, final CanvasElement element);

    @Override
    LayoutCanvas position(final UnaryOperator<CanvasPosition> mutator);

    @Override
    LayoutCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);
}
