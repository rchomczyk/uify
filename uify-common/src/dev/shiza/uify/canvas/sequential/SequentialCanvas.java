package dev.shiza.uify.canvas.sequential;

import dev.shiza.uify.annotation.Mutable;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;

@Mutable
public interface SequentialCanvas extends Canvas {

    static SequentialCanvas ofRows(final int rows) {
        return new SequentialCanvasImpl(new ArrayList<>())
            .position(position -> position.minimum(0, 0).maximum(rows - 1, 8));
    }

    SequentialCanvas elements(final CanvasElement... elements);

    SequentialCanvas elements(final Collection<CanvasElement> elements);

    SequentialCanvas elements(final Collection<CanvasElement> elements, final boolean override);

    @Override
    SequentialCanvas position(final UnaryOperator<CanvasPosition> mutator);
}
