package dev.shiza.uify.canvas.paginated;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;
import org.bukkit.event.inventory.InventoryCloseEvent;

final class PaginatedCanvasImpl extends BaseCanvas implements PaginatedCanvas {

    private static final String ROW_DELIMITER = "\n";
    private final List<CanvasPosition> compositions;
    private final List<CanvasElement> rawElements;
    private List<List<CanvasElement>> partitionedElements;
    private PrecalculatedSlotIndex[] precalculatedSlotIndexes;
    private PredefinedBindings predefinedBindings;
    private NavigationalItemBinding forwardItemBinding;
    private NavigationalItemBinding backwardItemBinding;
    private int currentPage;

    PaginatedCanvasImpl(
        final List<CanvasPosition> compositions,
        final List<CanvasElement> rawElements,
        final List<List<CanvasElement>> partitionedElements,
        final PrecalculatedSlotIndex[] precalculatedSlotIndexes,
        final int currentPage) {
        this.compositions = compositions;
        this.rawElements = rawElements;
        this.partitionedElements = partitionedElements;
        this.precalculatedSlotIndexes = precalculatedSlotIndexes;
        this.currentPage = currentPage;
    }

    PaginatedCanvasImpl() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new PrecalculatedSlotIndex[0], 0);
    }

    static PaginatedCanvas ofPattern(final String pattern) {
        final String[] rows = pattern.split(ROW_DELIMITER);

        final PaginatedCanvasImpl paginatedCanvas = new PaginatedCanvasImpl();

        paginatedCanvas.predefinedBindings = positionsFromPattern(rows);
        for (final Position position : paginatedCanvas.predefinedBindings.contentPositions()) {
            paginatedCanvas.compose(__ -> CanvasPosition.pos(position));
        }

        if (rows.length == 0) {
            throw new PaginatedCanvasBindingException("The pattern does not contain any rows.");
        }

        final int columnsPerRow = rows[0].length();
        return paginatedCanvas
            .position(position ->
                position
                    .minimum(0, 0)
                    .maximum(rows.length - 1, columnsPerRow - 1));
    }

    static PaginatedCanvas ofPattern(final String... patterns) {
        return ofPattern(String.join(ROW_DELIMITER, patterns));
    }

    private static PredefinedBindings positionsFromPattern(final String[] rows) {
        NavigationalSlotIndexes navbarPositions = new NavigationalSlotIndexes(null, null);

        final List<Position> activePositions = new ArrayList<>();
        for (int row = 0; row < rows.length; row++) {
            for (int column = 0; column < rows[row].length(); column++) {
                switch (rows[row].charAt(column)) {
                    case 'x' -> activePositions.add(new Position(row, column));
                    case ' ' -> {
                        // ignored
                    }
                    case '>' -> {
                        if (navbarPositions.forward() != null) {
                            throw new PaginatedCanvasBindingException(
                                "The pattern contains more than one navigating symbol at row %d, column %d.".formatted(
                                    row, column));
                        }
                        navbarPositions =
                            new NavigationalSlotIndexes(new Position(row, column), navbarPositions.backward());
                    }
                    case '<' -> {
                        if (navbarPositions.backward() != null) {
                            throw new PaginatedCanvasBindingException(
                                "The pattern contains more than one navigating symbol at row %d, column %d.".formatted(
                                    row, column));
                        }
                        navbarPositions =
                            new NavigationalSlotIndexes(navbarPositions.forward(), new Position(row, column));
                    }
                    default -> throw new PaginatedCanvasBindingException(
                        "The pattern contains invalid symbol at row %d, column %d.".formatted(row, column));
                }
            }
        }

        if (activePositions.isEmpty()) {
            throw new PaginatedCanvasBindingException(
                "The pattern does not contain any active positions. Use 'x' to mark active positions.");
        }

        if (navbarPositions.forward() == null && navbarPositions.backward() == null) {
            throw new PaginatedCanvasBindingException(
                "The pattern does not contain any navigating symbols. Use '>' or '<' to mark the navigating symbols.");
        }

        return new PredefinedBindings(activePositions, navbarPositions);
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

        final int elementsPerPartition = compositions.isEmpty()
            ? calculateSlotsForPosition(position())
            : compositions.stream().mapToInt(this::calculateSlotsForPosition).sum();
        partitionedElements = PaginatedCanvasUtils.partition(rawElements, elementsPerPartition);
        precalculatedSlotIndexes = calculatePrecalculatedIndexes(elementsPerPartition);
        return this;
    }

    @Override
    public PaginatedCanvas page(final int page) {
        if (page < 0 || page >= partitionedElements.size()) {
            throw new PaginatedCanvasBindingException("Invalid page number: " + page);
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
    public PaginatedCanvas forward(final CanvasElement element) {
        final Position position = predefinedBindings.navbarPositions().forward();
        if (position == null) {
            throw new PaginatedCanvasBindingException(
                "Navbar position is not defined. Use PaginatedCanvas#forward(int, int, CanvasElement) to define it.");
        }

        return forward(position.row(), position.column(), element);
    }

    @Override
    public PaginatedCanvas forward(final int row, final int column, final CanvasElement element) {
        forwardItemBinding = new NavigationalItemBinding(
            row, column,
            element.onElementClick((state, event) -> {
                final int originalPage = pageCurrent();
                if (originalPage != forward().pageCurrent()) {
                    state.canvas().update();
                }
            }));
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
    public PaginatedCanvas backward(final CanvasElement element) {
        final Position position = predefinedBindings.navbarPositions().backward();
        if (position == null) {
            throw new PaginatedCanvasBindingException(
                "Navbar position is not defined. Use PaginatedCanvas#backward(int, int, CanvasElement) to define it.");
        }

        return backward(position.row(), position.column(), element);
    }

    @Override
    public PaginatedCanvas backward(final int row, final int column, final CanvasElement element) {
        backwardItemBinding = new NavigationalItemBinding(
            row, column,
            element.onElementClick((state, event) -> {
                final int originalPage = pageCurrent();
                if (originalPage != backward().pageCurrent()) {
                    state.canvas().update();
                }
            }));
        return this;
    }

    @Override
    public PaginatedCanvas compose(final UnaryOperator<CanvasPosition> mutator) {
        compositions.add(mutator.apply(new CanvasPosition()));
        return this;
    }

    @Override
    public PaginatedCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    @Override
    public PaginatedCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour) {
        super.onCanvasClose(canvasCloseBehaviour);
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

    NavigationalItemBinding forwardItemBinding() {
        return forwardItemBinding;
    }

    NavigationalItemBinding backwardItemBinding() {
        return backwardItemBinding;
    }

    private PrecalculatedSlotIndex[] calculatePrecalculatedIndexes(final int totalSlots) {
        final PrecalculatedSlotIndex[] slotIndexes = new PrecalculatedSlotIndex[totalSlots];

        int index = 0;
        for (final CanvasPosition position : positionsToProcess()) {
            index = position.maximum().equals(position.minimum())
                ? fillIndexesForDirectPosition(position, slotIndexes, index)
                : fillIndexesForPosition(position, slotIndexes, index);
        }

        return slotIndexes;
    }

    private List<CanvasPosition> positionsToProcess() {
        return compositions.isEmpty() ? List.of(position()) : compositions;
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

    private int fillIndexesForDirectPosition(
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

    record NavigationalSlotIndexes(Position forward, Position backward) {}

    record NavigationalItemBinding(int row, int column, CanvasElement element) {}

    record PredefinedBindings(List<Position> contentPositions, NavigationalSlotIndexes navbarPositions) {}
}