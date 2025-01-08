package dev.shiza.uify.canvas.paginated;

import dev.shiza.uify.annotation.Mutable;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Collection;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;

@Mutable
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

    static PaginatedCanvas ofPattern(final String pattern) {
        return PaginatedCanvasImpl.ofPattern(pattern);
    }

    static PaginatedCanvas ofPattern(final String... patterns) {
        return PaginatedCanvasImpl.ofPattern(patterns);
    }

    PaginatedCanvas page(final int page);

    PaginatedCanvas forward();

    PaginatedCanvas bindForward(final CanvasElement element);

    PaginatedCanvas bindForward(final int row, final int column, final CanvasElement element);

    PaginatedCanvas backward();

    PaginatedCanvas bindBackward(final CanvasElement element);

    PaginatedCanvas bindBackward(final int row, final int column, final CanvasElement element);

    PaginatedCanvas populate(final Collection<? extends CanvasElement> elements);

    PaginatedCanvas populate(final Collection<? extends CanvasElement> elements, final boolean override);

    @Override
    PaginatedCanvas position(final UnaryOperator<CanvasPosition> mutator);

    PaginatedCanvas positionInner(final UnaryOperator<CanvasPosition> canvasPosition);

    int pages();

    int pageCurrent();
}
