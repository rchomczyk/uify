package dev.shiza.uify.canvas;

import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;
import dev.shiza.uify.position.Position;

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

    public record CanvasPosition(Position minimum, Position maximum) {

        private static final int MINIMUM_COLUMN = 0;
        private static final int MAXIMUM_COLUMN = 8;

        public CanvasPosition() {
            this(null, null);
        }

        public static CanvasPosition row(final int row) {
            return new CanvasPosition(
                new Position(row, MINIMUM_COLUMN),
                new Position(row, MAXIMUM_COLUMN));
        }

        public CanvasPosition minimum(final int row, final int column) {
            return new CanvasPosition(new Position(row, column), maximum);
        }

        public CanvasPosition maximum(final int row, final int column) {
            return new CanvasPosition(minimum, new Position(row, column));
        }
    }
}
