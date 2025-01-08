package dev.shiza.uify.canvas.paginated;

import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.canvas.renderer.CanvasRenderingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;

final class PaginatedCanvasImpl extends BaseCanvas implements PaginatedCanvas {

    private static final String ROW_DELIMITER = "\n";
    private final List<CanvasPosition> innerPositions;
    private final List<CanvasElement> rawElements;
    private List<List<CanvasElement>> partitionedElements;
    private PrecalculatedSlotIndex[] precalculatedSlotIndexes;
    private int currentPage;

    PaginatedCanvasImpl(
        final List<CanvasPosition> innerPositions,
        final List<CanvasElement> rawElements,
        final List<List<CanvasElement>> partitionedElements,
        final PrecalculatedSlotIndex[] precalculatedSlotIndexes,
        final int currentPage) {
        this.innerPositions = innerPositions;
        this.rawElements = rawElements;
        this.partitionedElements = partitionedElements;
        this.precalculatedSlotIndexes = precalculatedSlotIndexes;
        this.currentPage = currentPage;
    }

    PaginatedCanvasImpl() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new PrecalculatedSlotIndex[0], 0);
    }

    static PaginatedCanvas ofPattern(final char symbol, final String pattern) {
        final String[] rows = pattern.split(ROW_DELIMITER);

        final List<Position> activePositions = positionsFromPattern(symbol, rows);

        final PaginatedCanvasImpl paginatedCanvas = new PaginatedCanvasImpl();
        for (final Position position : activePositions) {
            paginatedCanvas.positionInner(__ -> CanvasPosition.pos(position));
        }

        if (rows.length == 0) {
            throw new CanvasRenderingException("The pattern does not contain any rows.");
        }

        final int columnsPerRow = rows[0].length();
        return paginatedCanvas
            .position(position ->
                position
                    .minimum(0, 0)
                    .maximum(rows.length - 1, columnsPerRow - 1));
    }

    static PaginatedCanvas ofPattern(final char symbol, final String... patterns) {
        return ofPattern(symbol, String.join(ROW_DELIMITER, patterns));
    }

    private static List<Position> positionsFromPattern(final char symbol, final String[] rows) {
        final List<Position> activePositions = new ArrayList<>();
        for (int row = 0; row < rows.length; row++) {
            for (int column = 0; column < rows[row].length(); column++) {
                if (rows[row].charAt(column) == symbol) {
                    activePositions.add(new Position(row, column));
                }
            }
        }

        if (activePositions.isEmpty()) {
            throw new IllegalArgumentException("The pattern does not contain any active 'x' positions.");
        }

        return activePositions;
    }

    @Override
    public PaginatedCanvas populate(final Collection<? extends CanvasElement> elements) {
        return populate(elements, false);
    }

    @Override
    public PaginatedCanvas populate(final Collection<? extends CanvasElement> elements, final boolean override) {
        if (override) {
            rawElements.clear();
        }
        rawElements.addAll(elements);

        final int elementsPerPartition = innerPositions.isEmpty()
            ? calculateSlotsForPosition(position())
            : innerPositions.stream().mapToInt(this::calculateSlotsForPosition).sum();
        partitionedElements = PaginatedCanvasUtils.partition(rawElements, elementsPerPartition);
        precalculatedSlotIndexes = calculatePrecalculatedIndexes(elementsPerPartition);
        return this;
    }

    @Override
    public PaginatedCanvas page(final int page) {
        if (page < 0 || page >= partitionedElements.size()) {
            throw new CanvasRenderingException("Invalid page number: " + page);
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
    public CanvasMapperRenderer mapper() {
        return PaginatedCanvasRenderer.InstanceHolder.MAPPER;
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

        int currentRow = 0;
        int currentColumn = 0;
        while (currentRow <= (maximum.row() - minimum.row())) {
            while (currentColumn <= (maximum.column() - minimum.column())) {
                final int slotIndex = currentRow * totalColumns + currentColumn;
                slotIndexes[index++] = new PrecalculatedSlotIndex(currentRow, currentColumn, slotIndex);
                currentColumn++;
            }

            currentColumn = 0;
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