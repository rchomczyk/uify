package pl.auroramc.ui.canvas.paginated;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import pl.auroramc.ui.canvas.CanvasWithPosition;
import pl.auroramc.ui.canvas.element.CanvasElement;
import pl.auroramc.ui.position.Position;

final class PaginatedCanvasImpl extends CanvasWithPosition implements PaginatedCanvas {

    private List<List<CanvasElement>> partitionedElements;
    private PrecalculatedSlotIndex[] precalculatedSlotIndexes;
    private int currentPage = 0;

    PaginatedCanvasImpl() {
    }

    @Override
    public PaginatedCanvas populate(final Collection<CanvasElement> elements) {
        final int elementsPerPartition = elementsPerPage();
        this.partitionedElements = PaginatedCanvasUtils.partition(elements, elementsPerPartition);
        this.precalculatedSlotIndexes = new PrecalculatedSlotIndex[elementsPerPartition];
        this.precalculateSlotIndexes();
        return this;
    }

    @Override
    public PaginatedCanvas page(final int page) {
        if (page < 0 || page >= partitionedElements.size()) {
            throw new PaginatedCanvasRenderingException(
                "Could not render paginated canvas, because of invalid page number.");
        }

        currentPage = page;
        return this;
    }

    @Override
    public PaginatedCanvas forward() {
        if (currentPage < partitionedElements.size() - 1) {
            currentPage++;
        }
        return this;
    }

    @Override
    public PaginatedCanvas backward() {
        if (currentPage > 0) {
            currentPage--;
        }
        return this;
    }

    @Override
    public PaginatedCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    @Override
    public int pages() {
        return partitionedElements.size();
    }

    @Override
    public int pageCurrent() {
        return currentPage;
    }

    List<List<CanvasElement>> partitionedElements() {
        return partitionedElements;
    }

    PrecalculatedSlotIndex[] precalculatedSlotIndexes() {
        return precalculatedSlotIndexes;
    }

    int currentPage() {
        return currentPage;
    }

    private void precalculateSlotIndexes() {
        int currentRow = 0;
        int currentColumn = 0;

        final int totalColumns = position().maximum().column() + 1;
        for (int index = 0; index < precalculatedSlotIndexes.length; index++) {
            if (currentColumn == totalColumns) {
                currentColumn = 0;
                currentRow++;
            }

            final int slotIndex = currentRow * totalColumns + currentColumn;
            precalculatedSlotIndexes[index] = new PrecalculatedSlotIndex(
                currentRow,
                currentColumn,
                slotIndex);
            currentColumn++;
        }
    }

    private int elementsPerPage() {
        final Position minimum = position().minimum();
        final Position maximum = position().maximum();
        if (minimum == null || maximum == null) {
            throw new PaginatedCanvasPartitioningException(
                "Could not partition elements for paginated canvas, because of missing minimum and maximum bounds. In" +
                    " case if you are not willing to define paginated canvas as an inner canvas, you should use " +
                    "PaginatedCanvas.ofRows() method instead of PaginatedCanvas.of()");
        }

        return (maximum.row() - minimum.row() + 1) * (maximum.column() - minimum.column() + 1);
    }

    record PrecalculatedSlotIndex(int row, int column, int slotIndex) {}
}
