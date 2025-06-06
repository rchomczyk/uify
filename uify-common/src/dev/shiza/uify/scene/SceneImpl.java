package dev.shiza.uify.scene;

import dev.shiza.uify.Uify;
import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.behaviour.tick.CanvasTickBehaviour;
import dev.shiza.uify.canvas.behaviour.tick.CanvasTickBehaviourState;
import dev.shiza.uify.canvas.element.cooldown.expiration.ExpirationScheduler;
import dev.shiza.uify.canvas.element.cooldown.expiration.ExpiringMap;
import dev.shiza.uify.canvas.element.cooldown.expiration.PaperExpirationSchedulerAccessor;
import dev.shiza.uify.scene.behaviour.SceneGenericBehaviour;
import dev.shiza.uify.scene.behaviour.tick.SceneTickBehaviour;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import dev.shiza.uify.scene.renderer.SceneRenderer;
import dev.shiza.uify.scene.tick.SceneTickRegistry;
import dev.shiza.uify.scene.view.AnvilView;
import dev.shiza.uify.scene.view.SceneView;
import dev.shiza.uify.time.MinecraftTimeEquivalent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record SceneImpl(SceneView view,
                        Component title,
                        List<Canvas> canvases,
                        List<Player> viewers,
                        SceneGenericBehaviour<InventoryOpenEvent> sceneDispatchBehaviour,
                        SceneGenericBehaviour<InventoryCloseEvent> sceneCloseBehaviour,
                        SceneTickBehaviour sceneTickBehaviour)
    implements Scene {

    @Override
    public Scene view(final SceneView view) {
        return new SceneImpl(
            view,
            title,
            canvases,
            viewers,
            sceneDispatchBehaviour,
            sceneCloseBehaviour,
            sceneTickBehaviour);
    }

    @Override
    public Scene title(final Component title) {
        return new SceneImpl(
            view,
            title,
            canvases,
            viewers,
            sceneDispatchBehaviour,
            sceneCloseBehaviour,
            sceneTickBehaviour);
    }

    @Override
    public Scene canvas(final Canvas canvas) {
        final List<Canvas> mutableCanvases = new ArrayList<>(canvases);
        mutableCanvases.add(canvas);
        return new SceneImpl(
            view,
            title,
            mutableCanvases,
            viewers,
            sceneDispatchBehaviour,
            sceneCloseBehaviour,
            sceneTickBehaviour);
    }

    @Override
    public Scene viewer(final Player viewer) {
        if (view instanceof AnvilView) {
            throw new SceneComposingException("You cannot add more than one viewer to anvil view.");
        }

        final List<Player> mutableViewers = new ArrayList<>(viewers);
        mutableViewers.add(viewer);
        return new SceneImpl(
            view,
            title,
            canvases,
            mutableViewers,
            sceneDispatchBehaviour,
            sceneCloseBehaviour,
            sceneTickBehaviour);
    }

    @Override
    public Scene onSceneDispatch(final SceneGenericBehaviour<InventoryOpenEvent> sceneDispatchBehaviour) {
        return new SceneImpl(
            view,
            title,
            canvases,
            viewers,
            this.sceneDispatchBehaviour.andThen(sceneDispatchBehaviour),
            sceneCloseBehaviour,
            sceneTickBehaviour);
    }

    @Override
    public Scene onSceneClose(final SceneGenericBehaviour<InventoryCloseEvent> sceneCloseBehaviour) {
        return new SceneImpl(
            view,
            title,
            canvases,
            viewers,
            sceneDispatchBehaviour,
            this.sceneCloseBehaviour.andThen(sceneCloseBehaviour),
            sceneTickBehaviour);
    }

    @Override
    public Scene onSceneTick(final SceneTickBehaviour sceneTickBehaviour) {
        return new SceneImpl(
            view,
            title,
            canvases,
            viewers,
            sceneDispatchBehaviour,
            sceneCloseBehaviour,
            this.sceneTickBehaviour.andThen(sceneTickBehaviour));
    }

    @Override
    public void dispatch() {
        final Scene sceneMorph = this.onSceneTick(delegatingSceneTickBehaviour());
        final SceneInventoryHolder sceneInventoryHolder = SceneRenderer.sceneRenderer().renderScene(sceneMorph);
        final SceneImpl scene = (SceneImpl) sceneInventoryHolder.sceneMorph();

        final boolean hasDefinedTickBehaviour = !scene.sceneTickBehaviour().equals(SceneTickBehaviour.undefined());
        if (Uify.hasTickingSupport() && hasDefinedTickBehaviour) {
            SceneTickRegistry.sceneTickRegistry().register(sceneInventoryHolder);
        }

        if (scene.view() instanceof AnvilView) {
            sceneInventoryHolder.anvilInventory().open();
            return;
        }

        viewers.forEach(viewer -> viewer.openInventory(sceneInventoryHolder.getInventory()));
    }

    private SceneTickBehaviour delegatingSceneTickBehaviour() {
        final ExpirationScheduler expirationScheduler = PaperExpirationSchedulerAccessor.paperExpirationScheduler();
        final ExpiringMap<Integer, Instant> expiringMap =
            new ExpiringMap<>(
                Duration.ZERO,
                Duration.ofMillis(MinecraftTimeEquivalent.MILLISECONDS_PER_TICK),
                expirationScheduler);
        return holder -> {
            final Map<Canvas, CanvasTickBehaviour<? extends Canvas>> canvasTickBehaviours =
                ((SceneImpl) holder.sceneMorph()).canvases().stream()
                    .filter(Predicate.not(canvas -> ((BaseCanvas) canvas).canvasTickBehaviour()
                        .equals(CanvasTickBehaviour.undefined())))
                    .filter(Predicate.not(canvas -> ((BaseCanvas) canvas).canvasType() == null))
                    .collect(Collectors.toMap(
                        Function.identity(),
                        canvas -> ((BaseCanvas) canvas).canvasTickBehaviour()));
            if (canvasTickBehaviours.isEmpty()) {
                return;
            }

            final LongAdder index = new LongAdder();
            canvasTickBehaviours.forEach((canvas, canvasTickBehaviour) -> {
                index.increment();

                final Class<? extends Canvas> canvasType = ((BaseCanvas) canvas).canvasType();
                if (canvasType != null) {
                    final int currentIndex = index.intValue();

                    final Instant now = Instant.now();
                    final Instant expirationTime = expiringMap.get(currentIndex);
                    if (expirationTime != null && expirationTime.isAfter(now)) {
                        return;
                    }

                    // noinspection unchecked
                    final CanvasTickBehaviour<Canvas> safeTickBehaviour =
                        (CanvasTickBehaviour<Canvas>) canvasTickBehaviour;
                    final CanvasTickBehaviourState<Canvas> tickBehaviourState =
                        new CanvasTickBehaviourState<>(holder, canvas.typed(canvasType));

                    final Duration delay = safeTickBehaviour.accept(tickBehaviourState);
                    if (delay.compareTo(Duration.ZERO) > 0) {
                        expiringMap.put(currentIndex, now.plus(delay), delay);
                    }
                }
            });
        };
    }
}
