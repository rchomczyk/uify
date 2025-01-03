package dev.shiza.uify.canvas.sequential;

import dev.shiza.uify.canvas.CanvasWithPosition;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

final class SequentialCanvasImpl extends CanvasWithPosition implements SequentialCanvas {

    private final List<CanvasElement> elements;

    SequentialCanvasImpl(final List<CanvasElement> elements) {
        this.elements = Collections.unmodifiableList(elements);
    }

    @Override
    public SequentialCanvas element(final CanvasElement element) {
        final List<CanvasElement> mutableElements = new ArrayList<>(this.elements);
        mutableElements.add(element);
        return new SequentialCanvasImpl(mutableElements).position(__ -> super.position());
    }

    @Override
    public SequentialCanvas elements(final List<CanvasElement> elements) {
        final List<CanvasElement> mutableElements = new ArrayList<>(this.elements);
        mutableElements.addAll(elements);
        return new SequentialCanvasImpl(mutableElements).position(__ -> super.position());
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
