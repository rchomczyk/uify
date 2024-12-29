package dev.shiza.uify.scene;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import dev.shiza.uify.scene.renderer.SceneRenderer;
import dev.shiza.uify.scene.view.SceneView;

@ApiStatus.Internal
public record SceneImpl(SceneView view, Component title, List<Canvas> canvases, List<Player> viewers)
    implements Scene {

    @Override
    public Scene view(final SceneView view) {
        return new SceneImpl(view, title, canvases, viewers);
    }

    @Override
    public Scene title(final Component title) {
        return new SceneImpl(view, title, canvases, viewers);
    }

    @Override
    public Scene canvas(final Canvas canvas) {
        final List<Canvas> mutableCanvases = new ArrayList<>(canvases);
        mutableCanvases.add(canvas);
        return new SceneImpl(view, title, mutableCanvases, viewers);
    }

    @Override
    public Scene viewer(final Player viewer) {
        final List<Player> mutableViewers = new ArrayList<>(viewers);
        mutableViewers.add(viewer);
        return new SceneImpl(view, title, canvases, mutableViewers);
    }

    @Override
    public void dispatch() {
        final SceneInventoryHolder sceneInventoryHolder = SceneRenderer.sceneRenderer().renderScene(this);
        viewers.forEach(viewer -> viewer.openInventory(sceneInventoryHolder.getInventory()));
    }
}
