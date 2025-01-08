package dev.shiza.uify.canvas.sequential;

import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.UnaryOperator;

final class SequentialCanvasImpl extends BaseCanvas implements SequentialCanvas {

    private final Collection<CanvasElement> mutableElements;

    SequentialCanvasImpl(final Collection<CanvasElement> elements) {
        this.mutableElements = elements;
    }

    @Override
    public SequentialCanvas elements(final CanvasElement... elements) {
        return elements(Arrays.asList(elements));
    }

    @Override
    public SequentialCanvas elements(final Collection<CanvasElement> elements) {
        return elements(elements, false);
    }

    @Override
    public SequentialCanvas elements(final Collection<CanvasElement> elements, final boolean override) {
        if (override) {
            elements.clear();
        }
        this.mutableElements.addAll(elements);
        return this;
    }

    @Override
    public SequentialCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    @Override
    public CanvasMapperRenderer mapper() {
        return SequentialCanvasRenderer.InstanceHolder.MAPPER;
    }

    Collection<CanvasElement> elements() {
        return mutableElements;
    }
}
