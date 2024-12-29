package pl.auroramc.ui.canvas.layout;

import java.util.Collections;
import java.util.function.UnaryOperator;
import pl.auroramc.ui.canvas.Canvas;
import pl.auroramc.ui.canvas.CanvasWithPosition.CanvasPosition;
import pl.auroramc.ui.canvas.element.CanvasElement;

public interface LayoutCanvas extends Canvas {

    static LayoutCanvas ofPattern(final String pattern) {
        return new LayoutCanvasImpl(pattern, Collections.emptyMap(), Collections.emptyMap());
    }

    LayoutCanvas bind(final int row, final int column, final CanvasElement element);

    LayoutCanvas bind(final char source, final CanvasElement element);

    @Override
    LayoutCanvas position(final UnaryOperator<CanvasPosition> mutator);
}
