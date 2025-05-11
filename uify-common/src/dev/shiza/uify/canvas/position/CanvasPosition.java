package dev.shiza.uify.canvas.position;

import dev.shiza.uify.position.Position;
import java.util.Objects;
import org.jetbrains.annotations.Range;

public record CanvasPosition(@Range(from = 0, to = 5) Position minimum, @Range(from = 0, to = 8) Position maximum) {

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

    public static CanvasPosition pos(final Position position) {
        return new CanvasPosition(position, position);
    }

    public CanvasPosition minimum(final @Range(from = 0, to = 5) int row, final @Range(from = 0, to = 8) int column) {
        return new CanvasPosition(new Position(row, column), maximum);
    }

    public CanvasPosition maximum(final @Range(from = 0, to = 5) int row, final @Range(from = 0, to = 8) int column) {
        return new CanvasPosition(minimum, new Position(row, column));
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final CanvasPosition that = (CanvasPosition) object;
        return Objects.equals(minimum, that.minimum) && Objects.equals(maximum, that.maximum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimum, maximum);
    }
}