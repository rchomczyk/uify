package dev.shiza.uify.canvas;

import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.CanvasWithPosition.CanvasPosition;

public interface Canvas {

    Canvas position(final UnaryOperator<CanvasPosition> mutator);
}
