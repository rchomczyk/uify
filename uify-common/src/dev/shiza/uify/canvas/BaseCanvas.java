package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class BaseCanvas implements Canvas {

    private CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour = (state, event) -> {};
    private CanvasPosition canvasPosition = new CanvasPosition();

    @Override
    public Canvas position(final UnaryOperator<CanvasPosition> mutator) {
        this.canvasPosition = mutator.apply(this.canvasPosition);
        return this;
    }

    public CanvasPosition position() {
        return canvasPosition;
    }

    @Override
    public <T extends Canvas> T typed(final Class<T> canvasType) {
        return mapping(canvasType)
            .orElseThrow(() -> new CanvasTypingException("Could not type canvas as %s.".formatted(canvasType.getName())));
    }

    @Override
    public <T extends Canvas> Optional<T> mapping(final Class<T> canvasType) {
        return Optional.of(this).filter(canvasType::isInstance).map(canvasType::cast);
    }

    @Override
    public <T extends Canvas, R> Optional<R> mapping(final Class<T> canvasType, final Function<T, R> mutator) {
        return mapping(canvasType).map(mutator);
    }

    @Override
    public Canvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour) {
        this.canvasCloseBehaviour = this.canvasCloseBehaviour.andThen(canvasCloseBehaviour);
        return this;
    }

    public CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour() {
        return canvasCloseBehaviour;
    }
}
