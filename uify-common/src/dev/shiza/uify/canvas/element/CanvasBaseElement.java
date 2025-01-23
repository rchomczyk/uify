package dev.shiza.uify.canvas.element;

import dev.shiza.uify.canvas.element.behaviour.CanvasElementGenericBehaviour;
import dev.shiza.uify.canvas.element.cooldown.CooldownGenericBehaviour;
import io.papermc.paper.util.Tick;
import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.Canvas;
import org.bukkit.plugin.Plugin;

public class CanvasBaseElement implements CanvasElement {

    private Supplier<ItemStack> itemStack;
    private CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragBehaviour =
        (state, event) -> {};
    private CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickBehaviour =
        (state, event) -> {};
    private CooldownGenericBehaviour<Canvas, InventoryClickEvent> elementCooldownBehaviour =
        (state, event) -> {};
    private Duration cooldown = Duration.ZERO;
    private final Set<Canvas> owners = new HashSet<>();
    private final AtomicBoolean transforming = new AtomicBoolean(false);

    public CanvasBaseElement(final Supplier<ItemStack> itemStack) {
        this.itemStack = itemStack;
    }

    public CanvasBaseElement(
        final Supplier<ItemStack> itemStack,
        final CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragBehaviour,
        final CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickBehaviour,
        final CooldownGenericBehaviour<Canvas, InventoryClickEvent> elementCooldownBehaviour) {
        this.itemStack = itemStack;
        this.elementDragBehaviour = elementDragBehaviour;
        this.elementClickBehaviour = elementClickBehaviour;
        this.elementCooldownBehaviour = elementCooldownBehaviour;
    }

    public CanvasBaseElement(final CanvasElement element) {
        this(
            ((CanvasBaseElement) element).itemStack,
            ((CanvasBaseElement) element).elementDragBehaviour,
            ((CanvasBaseElement) element).elementClickBehaviour,
            ((CanvasBaseElement) element).elementCooldownBehaviour);
    }

    @Override
    public CanvasElement cooldown(final Duration period) {
        this.cooldown = period;
        return this;
    }

    @Override
    public ItemStack renderElement(final Scene parentScene, final Canvas parentCanvas) {
        return itemStack.get();
    }

    @Override
    public CanvasElement onElementDrag(final CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragBehaviour) {
        this.elementDragBehaviour = this.elementDragBehaviour.andThen(elementDragBehaviour);
        return this;
    }

    @Override
    public CanvasElement onElementClick(final CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickBehaviour) {
        this.elementClickBehaviour = this.elementClickBehaviour.andThen(elementClickBehaviour);
        return this;
    }

    @Override
    public CanvasElement onElementCooldown(final CooldownGenericBehaviour<Canvas, InventoryClickEvent> elementCooldownBehaviour) {
        this.elementCooldownBehaviour = this.elementCooldownBehaviour.andThen(elementCooldownBehaviour);
        return this;
    }

    @Override
    public void assign(final Canvas canvas) {
        owners.add(canvas);
    }

    @Override
    public void update() {
        owners.forEach(Canvas::update);
    }

    @Override
    public void transform(final Supplier<ItemStack> itemStack) {
        this.itemStack = itemStack;
        this.update();
    }

    @Override
    public void transform(final Plugin plugin, final Supplier<ItemStack> itemStack, final Duration period) {
        transform(plugin, itemStack, this.itemStack, period);
    }

    @Override
    public void transform(
        final Plugin plugin, final Supplier<ItemStack> from, final Supplier<ItemStack> to, final Duration period) {
        if (!transforming.compareAndSet(false, true)) {
            return;
        }

        if (!Objects.equals(from, this.itemStack)) {
            transform(from);
        }

        final long delayInTicks = Tick.tick().fromDuration(cooldown);
        plugin.getServer()
            .getScheduler()
            .runTaskLater(
                plugin, () -> {
                    transform(to);
                    transforming.set(false);
                }, delayInTicks);
    }

    public Duration cooldown() {
        return cooldown;
    }

    public CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragConsumer() {
        return elementDragBehaviour;
    }

    public CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickConsumer() {
        return elementClickBehaviour;
    }

    public CooldownGenericBehaviour<Canvas, InventoryClickEvent> elementCooldownBehaviour() {
        return elementCooldownBehaviour;
    }
}
