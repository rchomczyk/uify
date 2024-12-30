package dev.shiza.uify.canvas.sequential;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public interface SequentialCanvas extends Canvas {

    static SequentialCanvas ofElements(final List<CanvasElement> elements) {
        return new SequentialCanvasImpl(elements);
    }

    static SequentialCanvas of() {
        return ofElements(new ArrayList<>());
    }

    SequentialCanvas element(final CanvasElement element);

    SequentialCanvas elements(final List<CanvasElement> elements);

    @Override
    SequentialCanvas position(final UnaryOperator<CanvasPosition> mutator);
}
