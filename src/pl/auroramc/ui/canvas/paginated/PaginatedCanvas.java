package pl.auroramc.ui.canvas.paginated;

import java.util.Collection;
import java.util.function.UnaryOperator;
import pl.auroramc.ui.canvas.Canvas;
import pl.auroramc.ui.canvas.CanvasWithPosition.CanvasPosition;
import pl.auroramc.ui.canvas.element.CanvasElement;
import pl.auroramc.ui.position.Position;

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

    PaginatedCanvas page(final int page);

    PaginatedCanvas forward();

    PaginatedCanvas backward();

    PaginatedCanvas populate(final Collection<CanvasElement> elements);

    @Override
    PaginatedCanvas position(final UnaryOperator<CanvasPosition> mutator);

    int pages();

    int pageCurrent();
}
