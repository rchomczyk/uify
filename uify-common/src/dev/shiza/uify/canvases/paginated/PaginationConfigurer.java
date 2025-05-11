package dev.shiza.uify.canvases.paginated;

import dev.shiza.uify.canvas.element.CanvasElement;
import java.util.function.UnaryOperator;

public record PaginationConfigurer(PaginationButtonConfigurer forward, PaginationButtonConfigurer backward) {

    static PaginationConfigurer empty() {
        return new PaginationConfigurer(null, null);
    }

    public PaginationConfigurer forward(final UnaryOperator<PaginationButtonConfigurer> configurer) {
        final PaginationButtonConfigurer forward = configurer.apply(PaginationButtonConfigurer.empty());
        return new PaginationConfigurer(forward, this.backward());
    }

    public PaginationConfigurer backward(final UnaryOperator<PaginationButtonConfigurer> configurer) {
        final PaginationButtonConfigurer backward = configurer.apply(PaginationButtonConfigurer.empty());
        return new PaginationConfigurer(this.forward(), backward);
    }

    public record PaginationButtonConfigurer(int row, int column, CanvasElement element) {

        static PaginationButtonConfigurer empty() {
            return new PaginationButtonConfigurer(0, 0, null);
        }

        public PaginationButtonConfigurer row(final int row) {
            return new PaginationButtonConfigurer(row, this.column(), this.element());
        }

        public PaginationButtonConfigurer column(final int column) {
            return new PaginationButtonConfigurer(this.row(), column, this.element());
        }

        public PaginationButtonConfigurer element(final CanvasElement element) {
            return new PaginationButtonConfigurer(this.row(), this.column(), element);
        }
    }
}
