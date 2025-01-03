package dev.shiza.uify.canvas.layout;

import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import dev.shiza.uify.canvas.CanvasWithPosition;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;

final class LayoutCanvasImpl extends CanvasWithPosition implements LayoutCanvas {

    private final String pattern;
    private final Map<Character, CanvasElement> bindingsBySymbol;
    private final Map<Position, CanvasElement> bindingsByPosition;

    LayoutCanvasImpl(
        final String pattern,
        final Map<Character, CanvasElement> bindingsBySymbol,
        final Map<Position, CanvasElement> bindingsByPosition) {
        this.pattern = pattern;
        this.bindingsBySymbol = Collections.unmodifiableMap(bindingsBySymbol);
        this.bindingsByPosition = Collections.unmodifiableMap(bindingsByPosition);
    }

    @Override
    public LayoutCanvas bind(final int row, final int column, final CanvasElement element) {
        final Map<Position, CanvasElement> mutableBindings = new HashMap<>(this.bindingsByPosition);
        mutableBindings.put(new Position(row, column), element);
        return new LayoutCanvasImpl(pattern, bindingsBySymbol, mutableBindings)
            .position(__ -> super.position());
    }

    @Override
    public LayoutCanvas bind(final char source, final CanvasElement element) {
        final Map<Character, CanvasElement> mutableBindings = new HashMap<>(this.bindingsBySymbol);
        mutableBindings.put(source, element);
        return new LayoutCanvasImpl(pattern, mutableBindings, bindingsByPosition)
            .position(__ -> super.position());
    }

    @Override
    public LayoutCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    String pattern() {
        return pattern;
    }

    Map<Position, CanvasElement> bindingsByPosition() {
        return bindingsByPosition;
    }

    Map<Character, CanvasElement> bindingsBySymbol() {
        return bindingsBySymbol;
    }
}
