package dev.shiza.uify.canvas.sequential;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public interface SequentialCanvas extends Canvas {

    static SequentialCanvas ofRows(final int rows) {
        return new SequentialCanvasImpl(new ArrayList<>())
            .position(position -> position.minimum(0, 0).maximum(rows - 1, 8));
    }

    SequentialCanvas element(final CanvasElement element);

    SequentialCanvas elements(final List<CanvasElement> elements);

    @Override
    SequentialCanvas position(final UnaryOperator<CanvasPosition> mutator);
}
