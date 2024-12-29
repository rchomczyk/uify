package dev.shiza.uify.canvas.paginated;

import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.CanvasWithPosition;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;

final class PaginatedCanvasImpl extends CanvasWithPosition implements PaginatedCanvas {

    private final List<CanvasPosition> innerPositions;
    private List<List<CanvasElement>> partitionedElements;
    private PrecalculatedSlotIndex[] precalculatedSlotIndexes;
    private int currentPage;

    PaginatedCanvasImpl(
        final List<CanvasPosition> innerPositions,
        final List<List<CanvasElement>> partitionedElements,
        final PrecalculatedSlotIndex[] precalculatedSlotIndexes,
        final int currentPage) {
        this.innerPositions = innerPositions;
        this.partitionedElements = partitionedElements;
        this.precalculatedSlotIndexes = precalculatedSlotIndexes;
        this.currentPage = currentPage;
    }

    PaginatedCanvasImpl() {
        this(new ArrayList<>(), List.of(), new PrecalculatedSlotIndex[0], 0);
    }

    @Override
    public PaginatedCanvas populate(final Collection<CanvasElement> elements) {
        final int elementsPerPartition = innerPositions.isEmpty()
            ? calculateSlotsForPosition(position())
            : innerPositions.stream().mapToInt(this::calculateSlotsForPosition).sum();
        this.partitionedElements = PaginatedCanvasUtils.partition(elements, elementsPerPartition);
        this.precalculatedSlotIndexes = calculatePrecalculatedIndexes(elementsPerPartition);
        return this;
    }

    @Override
    public PaginatedCanvas page(final int page) {
        if (page < 0 || page >= partitionedElements.size()) {
            throw new PaginatedCanvasRenderingException("Invalid page number: " + page);
        }

        this.currentPage = page;
        return this;
    }

    @Override
    public PaginatedCanvas forward() {
        if (currentPage < pages() - 1) {
            this.currentPage++;
        }
        return this;
    }

    @Override
    public PaginatedCanvas backward() {
        if (currentPage > 0) {
            this.currentPage--;
        }
        return this;
    }

    @Override
    public PaginatedCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    @Override
    public PaginatedCanvas positionInner(final UnaryOperator<CanvasPosition> canvasPosition) {
        innerPositions.add(canvasPosition.apply(new CanvasPosition()));
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

    private PrecalculatedSlotIndex[] calculatePrecalculatedIndexes(final int totalSlots) {
        final PrecalculatedSlotIndex[] slotIndexes = new PrecalculatedSlotIndex[totalSlots];

        int index = 0;
        for (final CanvasPosition position : positionsToProcess()) {
            index = fillIndexesForPosition(position, slotIndexes, index);
        }

        return slotIndexes;
    }

    private List<CanvasPosition> positionsToProcess() {
        return innerPositions.isEmpty() ? List.of(position()) : innerPositions;
    }

    private int fillIndexesForPosition(
        final CanvasPosition position,
        final PrecalculatedSlotIndex[] slotIndexes,
        final int startIndex) {
        final int totalColumns = position.maximum().column() - position.minimum().column() + 1;
        final Position minimum = position.minimum();
        final Position maximum = position.maximum();

        int index = startIndex;
        int currentRow = minimum.row();
        int currentColumn = minimum.column();
        while (currentRow <= maximum.row()) {
            while (currentColumn <= maximum.column()) {
                final int slotIndex = (currentRow - minimum.row()) * totalColumns + (currentColumn - minimum.column());
                slotIndexes[index++] = new PrecalculatedSlotIndex(currentRow, currentColumn, slotIndex);
                currentColumn++;
            }
            currentColumn = minimum.column();
            currentRow++;
        }

        return index;
    }

    private int calculateSlotsForPosition(final CanvasPosition position) {
        final Position minimum = position.minimum();
        final Position maximum = position.maximum();
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