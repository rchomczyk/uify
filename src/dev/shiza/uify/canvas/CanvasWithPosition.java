package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class CanvasWithPosition implements Canvas {

    private CanvasPosition canvasPosition = new CanvasPosition();

    @Override
    public Canvas position(final UnaryOperator<CanvasPosition> mutator) {
        this.canvasPosition = mutator.apply(this.canvasPosition);
        return this;
    }

    public CanvasPosition position() {
        return canvasPosition;
    }
}
