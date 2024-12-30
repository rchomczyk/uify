package dev.shiza.uify.canvas.sequential;

import dev.shiza.uify.canvas.CanvasWithPosition;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.List;
import java.util.function.UnaryOperator;

final class SequentialCanvasImpl extends CanvasWithPosition implements SequentialCanvas {

    private final List<CanvasElement> elements;

    SequentialCanvasImpl(final List<CanvasElement> elements) {
        this.elements = elements;
    }

    @Override
    public SequentialCanvas element(final CanvasElement element) {
        this.elements.add(element);
        return this;
    }

    @Override
    public SequentialCanvas elements(final List<CanvasElement> elements) {
        this.elements.addAll(elements);
        return this;
    }

    @Override
    public SequentialCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    List<CanvasElement> elements() {
        return elements;
    }
}
