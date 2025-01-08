package dev.shiza.uify.canvas.position;

import dev.shiza.uify.position.Position;
import java.util.Objects;

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

    public static CanvasPosition pos(final Position position) {
        return new CanvasPosition(position, position);
    }

    public CanvasPosition minimum(final int row, final int column) {
        return new CanvasPosition(new Position(row, column), maximum);
    }

    public CanvasPosition maximum(final int row, final int column) {
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