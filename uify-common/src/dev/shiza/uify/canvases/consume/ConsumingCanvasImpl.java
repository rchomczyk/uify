package dev.shiza.uify.canvases.consume;

import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.CanvasComposingException;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviourState;
import dev.shiza.uify.canvas.element.CanvasBaseElement;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.position.PositionUtils;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

final class ConsumingCanvasImpl extends BaseCanvas implements ConsumingCanvas {

    private static final IndexedConsumptionResult EMPTY_INDEXED_CONSUMPTION_RESULT =
        new IndexedConsumptionResult(Collections.emptyMap(), Collections.emptyMap());
    private final Collection<CanvasElement> mutableElements;
    private final Map<Position, CanvasElement> mutableElementsByPosition;

    ConsumingCanvasImpl(
        final Collection<CanvasElement> elements, final Map<Position, CanvasElement> mutableElementsByPosition) {
        this.mutableElements = elements;
        this.mutableElementsByPosition = mutableElementsByPosition;
    }

    @Override
    public ConsumingCanvas populateItems(final Collection<ItemStack> items, final boolean override) {
        final List<CanvasBaseElement> itemsAsElements = items.stream()
            .map(item -> new CanvasBaseElement(() -> item))
            .toList();
        return populate(itemsAsElements, override);
    }

    @Override
    public ConsumingCanvas populateItems(
        final Map<Position, ? extends ItemStack> elements, final boolean override) {
        final Map<Position, CanvasElement> itemsAsElements = elements.entrySet().stream()
            .map(itemByLocalPosition -> new CanvasElementByPosition(
                itemByLocalPosition.getKey(),
                new CanvasBaseElement(itemByLocalPosition::getValue)))
            .collect(Collectors.toMap(CanvasElementByPosition::localPosition, CanvasElementByPosition::element));
        return populate(itemsAsElements, override);
    }

    @Override
    public ConsumingCanvas populate(final Collection<? extends CanvasElement> elements, final boolean override) {
        if (!mutableElementsByPosition.isEmpty()) {
            throw new CanvasComposingException(
                "Consuming canvas does not support mixing unordered and indexed population.");
        }

        if (override) {
            mutableElements.clear();
        }

        mutableElements.addAll(elements);
        return this;
    }

    @Override
    public ConsumingCanvas populate(final Map<Position, ? extends CanvasElement> elements, final boolean override) {
        if (!mutableElements.isEmpty()) {
            throw new CanvasComposingException(
                "Consuming canvas does not support mixing unordered and indexed population.");
        }

        if (override) {
            mutableElementsByPosition.clear();
        }

        mutableElementsByPosition.putAll(elements);
        return this;
    }

    @Override
    public ConsumingCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    @Override
    public ConsumingCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour) {
        super.onCanvasClose(canvasCloseBehaviour);
        return this;
    }

    @Override
    public ConsumingCanvas onConsumption(final ConsumptionBehaviour<ConsumptionResult> consumptionBehaviour) {
        super.onCanvasClose((state, event) -> {
            final CanvasGenericBehaviourState<ConsumingCanvas> typedState = ConsumptionBehaviour.typedCopy(state);
            consumptionBehaviour.accept(typedState, event, typedState.canvas().consume(state.holder()));
        });
        return this;
    }

    @Override
    public ConsumingCanvas onIndexedConsumption(final ConsumptionBehaviour<IndexedConsumptionResult> consumptionBehaviour) {
        super.onCanvasClose((state, event) -> {
            final CanvasGenericBehaviourState<ConsumingCanvas> typedState = ConsumptionBehaviour.typedCopy(state);
            consumptionBehaviour.accept(typedState, event, typedState.canvas().consumeWithIndexes(state.holder()));
        });
        return this;
    }

    @Override
    public CanvasMapperRenderer mapper() {
        return ConsumingCanvasRenderer.InstanceHolder.MAPPER;
    }

    @Override
    public ConsumptionResult consume(final SceneInventoryHolder holder) {
        return new ConsumptionResult(consumeWithIndexes(holder).itemsByRawSlots().entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .toList());
    }

    @Override
    public IndexedConsumptionResult consumeWithIndexes(final SceneInventoryHolder holder) {
        final Map<Position, ItemStack> consumedItems = new HashMap<>();
        final Map<Integer, ItemStack> consumedItemsByRawSlots = new HashMap<>();

        final CanvasPosition canvasPosition = position();
        if (canvasPosition == null) {
            return EMPTY_INDEXED_CONSUMPTION_RESULT;
        }

        final Position minimum = canvasPosition.minimum();
        final Position maximum = canvasPosition.maximum();
        if (minimum == null || maximum == null) {
            return EMPTY_INDEXED_CONSUMPTION_RESULT;
        }

        int localRow = 0;
        int localColumn;
        for (int row = minimum.row(); row <= maximum.row(); row++) {
            localColumn = 0;
            for (int column = minimum.column(); column <= maximum.column(); column++) {
                final int slot =
                    PositionUtils.calculateSlot(row, column, ((SceneImpl) holder.sceneMorph()).view().columnsPerRow());

                final ItemStack item = holder.getInventory().getItem(slot);
                if (item == null) {
                    localColumn++;
                    continue;
                }

                consumedItems.put(new Position(localRow, localColumn), item);
                consumedItemsByRawSlots.put(slot, item);

                localColumn++;
            }

            localRow++;
        }

        return new IndexedConsumptionResult(consumedItems, consumedItemsByRawSlots);
    }

    Collection<CanvasElement> elements() {
        return mutableElements;
    }

    Map<Position, CanvasElement> elementsByPosition() {
        return mutableElementsByPosition;
    }

    private record CanvasElementByPosition(Position localPosition, CanvasElement element) {}
}
