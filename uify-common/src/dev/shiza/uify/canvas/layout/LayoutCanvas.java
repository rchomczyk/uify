package dev.shiza.uify.canvas.layout;

import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Collections;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;

public interface LayoutCanvas extends Canvas {

    static LayoutCanvas pattern(final String pattern) {
        return new LayoutCanvasImpl(pattern, Collections.emptyMap(), Collections.emptyMap());
    }

    static LayoutCanvas pattern(final String... patterns) {
        return new LayoutCanvasImpl(String.join("\n", patterns), Collections.emptyMap(), Collections.emptyMap());
    }

    LayoutCanvas bind(final int row, final int column, final CanvasElement element);

    LayoutCanvas bind(final char source, final CanvasElement element);

    @Override
    LayoutCanvas position(final UnaryOperator<CanvasPosition> mutator);
}
