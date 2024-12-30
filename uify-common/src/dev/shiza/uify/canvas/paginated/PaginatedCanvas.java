package dev.shiza.uify.canvas.paginated;

import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Collection;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;
import org.jetbrains.annotations.ApiStatus;

public interface PaginatedCanvas extends Canvas {

    static PaginatedCanvas of() {
        return new PaginatedCanvasImpl();
    }

    static PaginatedCanvas ofRows(final int rows) {
        return new PaginatedCanvasImpl()
            .position(canvasPosition ->
                new CanvasPosition(
                    new Position(0, 0),
                    new Position(rows - 1, 8)));
    }

    @ApiStatus.Experimental
    static PaginatedCanvas ofPattern(final char symbol, final String pattern) {
        return PaginatedCanvasImpl.ofPattern(symbol, pattern);
    }

    PaginatedCanvas page(final int page);

    PaginatedCanvas forward();

    PaginatedCanvas backward();

    PaginatedCanvas populate(final Collection<CanvasElement> elements);

    @Override
    PaginatedCanvas position(final UnaryOperator<CanvasPosition> mutator);

    PaginatedCanvas positionInner(final UnaryOperator<CanvasPosition> canvasPosition);

    int pages();

    int pageCurrent();
}
