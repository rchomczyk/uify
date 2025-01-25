package dev.shiza.uify.canvas.layout;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Map;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;
import org.bukkit.event.inventory.InventoryCloseEvent;

class LayoutCanvasImpl extends BaseCanvas implements LayoutCanvas {

    private final String pattern;
    private final Map<Character, CanvasElement> mutableBindingsBySymbol;
    private final Map<Position, CanvasElement> mutableBindingsByPosition;

    LayoutCanvasImpl(
        final String pattern,
        final Map<Character, CanvasElement> mutableBindingsBySymbol,
        final Map<Position, CanvasElement> mutableBindingsByPosition) {
        this.pattern = pattern;
        this.mutableBindingsBySymbol = mutableBindingsBySymbol;
        this.mutableBindingsByPosition = mutableBindingsByPosition;
    }

    @Override
    public LayoutCanvas bind(final int row, final int column, final CanvasElement element) {
        mutableBindingsByPosition.put(new Position(row, column), element);
        return this;
    }

    @Override
    public LayoutCanvas bind(final char source, final CanvasElement element) {
        mutableBindingsBySymbol.put(source, element);
        return this;
    }

    @Override
    public LayoutCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    @Override
    public LayoutCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour) {
        super.onCanvasClose(canvasCloseBehaviour);
        return this;
    }

    @Override
    public CanvasMapperRenderer mapper() {
        return LayoutCanvasRenderer.InstanceHolder.MAPPER;
    }

    String pattern() {
        return pattern;
    }

    Map<Position, CanvasElement> bindingsByPosition() {
        return mutableBindingsByPosition;
    }

    Map<Character, CanvasElement> bindingsBySymbol() {
        return mutableBindingsBySymbol;
    }
}
