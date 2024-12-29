package pl.auroramc.ui.canvas;

import java.util.function.UnaryOperator;
import pl.auroramc.ui.canvas.CanvasWithPosition.CanvasPosition;

public interface Canvas {

    Canvas position(final UnaryOperator<CanvasPosition> mutator);
}
