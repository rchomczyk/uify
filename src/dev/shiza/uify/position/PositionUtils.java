package dev.shiza.uify.position;

import dev.shiza.uify.canvas.CanvasWithPosition.CanvasPosition;

public final class PositionUtils {

    private PositionUtils() {}

    public static int calculateSlot(final int row, final int column, final int columnsPerRow) {
        return row * columnsPerRow + column;
    }

    public static int calculateSlot(
        final CanvasPosition canvasPosition, final Position localPosition, final int columnsPerRow) {
        final int absoluteRow = canvasPosition.minimum().row() + localPosition.row();
        final int absoluteColumn = canvasPosition.minimum().column() + localPosition.column();
        return absoluteRow * columnsPerRow + absoluteColumn;
    }
}
