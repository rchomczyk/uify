package dev.shiza.uify.canvas.consume;

import dev.shiza.uify.canvas.BaseCanvas;
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
import org.bukkit.inventory.ItemStack;

final class ConsumingCanvasImpl extends BaseCanvas implements ConsumingCanvas {

    private final Collection<CanvasElement> mutableElements;

    ConsumingCanvasImpl(final Collection<CanvasElement> elements) {
        this.mutableElements = elements;
    }

    @Override
    public ConsumingCanvas populate(final Collection<CanvasElement> elements) {
        return populate(elements, false);
    }

    @Override
    public ConsumingCanvas populate(final Collection<CanvasElement> elements, final boolean override) {
        if (override) {
            mutableElements.clear();
        }
        mutableElements.addAll(elements);
        return this;
    }

    @Override
    public ConsumingCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    @Override
    public List<ItemStack> consume(final SceneInventoryHolder holder) {
        final Map<Integer, ItemStack> consumedItems = new HashMap<>();

        final CanvasPosition canvasPosition = position();
        if (canvasPosition == null) {
            return Collections.emptyList();
        }

        final Position minimum = canvasPosition.minimum();
        final Position maximum = canvasPosition.maximum();
        if (minimum == null || maximum == null) {
            return Collections.emptyList();
        }

        for (int row = minimum.row(); row <= maximum.row(); row++) {
            for (int column = minimum.column(); column <= maximum.column(); column++) {
                final int slot =
                    PositionUtils.calculateSlot(row, column, ((SceneImpl) holder.sceneMorph()).view().columnsPerRow());

                final ItemStack item = holder.getInventory().getItem(slot);
                if (item == null) {
                    continue;
                }

                consumedItems.put(slot, item);
            }
        }

        return consumedItems.entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .toList();
    }

    Collection<CanvasElement> elements() {
        return mutableElements;
    }
}
