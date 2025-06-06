package dev.shiza.uify.canvases.sequential;

import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryCloseEvent;

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
    public SequentialCanvas elements(final Collection<? extends CanvasElement> elements, final boolean override) {
        if (override) {
            mutableElements.clear();
        }

        mutableElements.addAll(elements);
        return this;
    }

    @Override
    public SequentialCanvas position(final UnaryOperator<CanvasPosition> mutator) {
        super.position(mutator);
        return this;
    }

    @Override
    public SequentialCanvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour) {
        super.onCanvasClose(canvasCloseBehaviour);
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
