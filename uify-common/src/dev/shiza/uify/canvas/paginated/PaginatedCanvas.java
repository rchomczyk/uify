package dev.shiza.uify.canvas.paginated;

import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.canvas.tick.CanvasTickBehaviour;
import java.util.Collection;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface PaginatedCanvas extends Canvas {

    static PaginatedCanvas self() {
        return new PaginatedCanvasImpl();
    }

    static PaginatedCanvas rows(final int rows) {
        return new PaginatedCanvasImpl()
            .position(canvasPosition ->
                new CanvasPosition(
                    new Position(0, 0),
                    new Position(rows - 1, 8)));
    }

    static PaginatedCanvas pattern(final String pattern) {
        return PaginatedCanvasImpl.ofPattern(pattern);
    }

    static PaginatedCanvas pattern(final String... patterns) {
        return PaginatedCanvasImpl.ofPattern(patterns);
    }

    default PaginatedCanvas populate(final Collection<? extends CanvasElement> elements) {
        return populate(elements, false);
    }

    default PaginatedCanvas onPaginatedCanvasTick(final CanvasTickBehaviour<PaginatedCanvas> canvasTickBehaviour) {
        onCanvasTick(PaginatedCanvas.class, canvasTickBehaviour);
        return this;
    }

    PaginatedCanvas page(final int page);

    PaginatedCanvas forward();

    PaginatedCanvas forward(final CanvasElement element);

    PaginatedCanvas forward(final int row, final int column, final CanvasElement element);

    PaginatedCanvas backward();

    PaginatedCanvas backward(final CanvasElement element);

    PaginatedCanvas backward(final int row, final int column, final CanvasElement element);

    PaginatedCanvas navigation(final UnaryOperator<PaginationConfigurer> configurator);

    PaginatedCanvas populate(final Collection<? extends CanvasElement> elements, final boolean override);

    PaginatedCanvas compose(final UnaryOperator<CanvasPosition> mutator);

    @Override
    PaginatedCanvas position(final UnaryOperator<CanvasPosition> mutator);

    @Override
    PaginatedCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);

    int pages();

    int pageCurrent();
}
