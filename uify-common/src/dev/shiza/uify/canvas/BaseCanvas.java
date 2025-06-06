package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.element.IdentifiedCanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.canvas.behaviour.tick.CanvasTickBehaviour;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class BaseCanvas implements Canvas {

    private CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour =
        CanvasGenericBehaviour.undefined();
    private CanvasTickBehaviour<? extends Canvas> canvasTickBehaviour = CanvasTickBehaviour.undefined();
    private CanvasPosition canvasPosition = new CanvasPosition();
    private Map<Integer, IdentifiedCanvasElement> renderedElements = Map.of();
    private Class<? extends Canvas> canvasTickType;
    private final Set<SceneInventoryHolder> owners = new HashSet<>();

    @Override
    public Canvas position(final UnaryOperator<CanvasPosition> mutator) {
        this.canvasPosition = mutator.apply(this.canvasPosition);
        return this;
    }

    public CanvasPosition position() {
        return canvasPosition;
    }

    @Override
    public void assign(final SceneInventoryHolder owner) {
        owners.add(owner);
    }

    @Override
    public void update() {
        owners.forEach(this::update);
    }

    private void update(final SceneInventoryHolder owner) {
        final Map<Integer, IdentifiedCanvasElement> currentOwnerElements = owner.renderedElements();
        final Map<Integer, IdentifiedCanvasElement> updatedOwnerElements = new HashMap<>(currentOwnerElements);

        final Map<Integer, IdentifiedCanvasElement> newCanvasElements =
            mapper().renderCanvas(owner, owner.sceneMorph(), this);
        renderedElements(newCanvasElements);

        updatedOwnerElements.entrySet().removeIf(entry -> entry.getValue().canvas().equals(this));
        updatedOwnerElements.putAll(newCanvasElements);
        owner.renderedElements(updatedOwnerElements);
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

    @Override
    public <T extends Canvas> Canvas onCanvasTick(
        final Class<T> canvasTickType, final CanvasTickBehaviour<T> canvasTickBehaviour) {
        if (this.canvasTickType != null && !this.canvasTickType.equals(canvasTickType)) {
            throw new CanvasTypingException("You cannot mix generic type for canvases.");
        }

        // noinspection unchecked
        this.canvasTickBehaviour = ((CanvasTickBehaviour<T>) this.canvasTickBehaviour).andThen(canvasTickBehaviour);
        this.canvasTickType = canvasTickType;
        return this;
    }

    public Set<SceneInventoryHolder> owners() {
        return owners;
    }

    public Map<Integer, IdentifiedCanvasElement> renderedElements() {
        return renderedElements;
    }

    public void renderedElements(final Map<Integer, IdentifiedCanvasElement> renderedElements) {
        this.renderedElements = Collections.unmodifiableMap(renderedElements);
    }

    public CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour() {
        return canvasCloseBehaviour;
    }

    public CanvasTickBehaviour<? extends Canvas> canvasTickBehaviour() {
        return canvasTickBehaviour;
    }

    public Class<? extends Canvas> canvasType() {
        return canvasTickType;
    }
}
