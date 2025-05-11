package dev.shiza.uify.scene;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.behaviour.SceneGenericBehaviour;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import dev.shiza.uify.scene.renderer.SceneRenderer;
import dev.shiza.uify.scene.tick.SceneTickBehaviour;
import dev.shiza.uify.scene.tick.SceneTickRegistry;
import dev.shiza.uify.scene.view.AnvilView;
import dev.shiza.uify.scene.view.SceneView;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.ApiStatus;

import static dev.shiza.uify.Uify.hasSceneTicking;

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
        final SceneInventoryHolder sceneInventoryHolder = SceneRenderer.sceneRenderer().renderScene(this);
        final SceneImpl scene = (SceneImpl) sceneInventoryHolder.sceneMorph();

        final boolean hasDefinedTickBehaviour = !scene.sceneTickBehaviour().equals(SceneTickBehaviour.undefined());
        if (hasSceneTicking() && hasDefinedTickBehaviour) {
            SceneTickRegistry.sceneTickRegistry().register(sceneInventoryHolder);
        }

        if (scene.view() instanceof AnvilView) {
            sceneInventoryHolder.anvilInventory().open();
            return;
        }

        viewers.forEach(viewer -> viewer.openInventory(sceneInventoryHolder.getInventory()));
    }
}
