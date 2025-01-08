package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Optional;
import java.util.function.UnaryOperator;

public interface Canvas {

    Canvas position(final UnaryOperator<CanvasPosition> mutator);

    <T extends Canvas> Optional<T> typed(final Class<T> canvasType);
}
