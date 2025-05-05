package dev.shiza.uify.canvas.layout;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public interface SelectingCanvas<N extends Number> extends LayoutCanvas {

    static <N extends Number> SelectingCanvas<N> pattern(final String pattern) {
        return new SelectingCanvasImpl<>(pattern, new HashMap<>(), new HashMap<>());
    }

    static <N extends Number> SelectingCanvas<N> pattern(final String... patterns) {
        return new SelectingCanvasImpl<>(String.join("\n", patterns), new HashMap<>(), new HashMap<>());
    }

    SelectingCanvas<N> mutator(
        final char source, final ItemStack itemStack, final UnaryOperator<N> mapper);

    SelectingCanvas<N> mutator(
        final char source, final CanvasElement element, final UnaryOperator<N> mapper);

    SelectingCanvas<N> display(final char source, final Function<N, CanvasElement> renderer);

    SelectingCanvas<N> amount(final N amount);

    SelectingCanvas<N> minimum(final N minimum);

    SelectingCanvas<N> maximum(final N maximum);

    @Override
    SelectingCanvas<N> position(final UnaryOperator<CanvasPosition> mutator);

    @Override
    SelectingCanvas<N> onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);

    N amount();
}
