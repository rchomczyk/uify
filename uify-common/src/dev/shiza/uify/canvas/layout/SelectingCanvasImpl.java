package dev.shiza.uify.canvas.layout;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasBaseElement;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.element.ImmutableCanvasElement;
import dev.shiza.uify.canvas.element.behaviour.CanvasElementGenericBehaviour;
import dev.shiza.uify.position.Position;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

final class SelectingCanvasImpl<N extends Number> extends LayoutCanvasImpl implements SelectingCanvas<N> {

    private final AtomicReference<N> amountReference;
    private final AtomicReference<N> minimumReference;
    private final AtomicReference<N> maximumReference;

    SelectingCanvasImpl(
        final String pattern,
        final Map<Character, CanvasElement> bindingsBySymbol,
        final Map<Position, CanvasElement> bindingsByPosition) {
        super(pattern, bindingsBySymbol, bindingsByPosition);
        this.amountReference = new AtomicReference<>();
        this.minimumReference = new AtomicReference<>();
        this.maximumReference = new AtomicReference<>();
    }

    @Override
    public SelectingCanvas<N> mutator(final char source, final ItemStack itemStack, final UnaryOperator<N> mapper) {
        final CanvasElement element = new ImmutableCanvasElement(() -> itemStack)
            .onElementClick((state, event) -> state.holder().update());
        return mutator(source, element, mapper);
    }

    @Override
    public SelectingCanvas<N> mutator(
        final char source, final CanvasElement element, final UnaryOperator<N> mapper) {
        final CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> mutator = (state, event) -> {
            final N amount = mapper.apply(amountReference.get());
            if (amount == null) {
                return;
            }

            final N maximum = maximumReference.get();
            final N minimum = minimumReference.get();

            double clampedValue = amount.doubleValue();
            if (maximum != null) {
                clampedValue = Math.min(clampedValue, maximum.doubleValue());
            }

            if (minimum != null) {
                clampedValue = Math.max(clampedValue, minimum.doubleValue());
            }

            amountReference.set(SelectingCanvasUtils.convertToNumberType(clampedValue, amount));
        };

        final CanvasBaseElement oldCanvasBaseElement = (CanvasBaseElement) element;
        final CanvasBaseElement newCanvasBaseElement = new CanvasBaseElement(
            oldCanvasBaseElement.itemStack(),
            oldCanvasBaseElement.elementDragConsumer(),
            mutator.andThen(oldCanvasBaseElement.elementClickConsumer()),
            oldCanvasBaseElement.elementCooldownBehaviour(),
            oldCanvasBaseElement.elementCooldownExpirationBehaviour());

        bind(source, newCanvasBaseElement);
        return this;
    }

    @Override
    public SelectingCanvas<N> amount(final N amount) {
        amountReference.set(amount);
        return this;
    }

    @Override
    public SelectingCanvas<N> minimum(final N minimum) {
        minimumReference.set(minimum);
        return this;
    }

    @Override
    public SelectingCanvas<N> maximum(final N maximum) {
        maximumReference.set(maximum);
        return this;
    }

    @Override
    public N amount() {
        return amountReference.get();
    }
}
