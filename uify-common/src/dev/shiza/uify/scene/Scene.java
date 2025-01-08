package dev.shiza.uify.scene;

import dev.shiza.uify.scene.behaviour.SceneGenericBehaviour;
import java.util.Collections;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import dev.shiza.uify.scene.view.ChestView;
import dev.shiza.uify.scene.view.SceneView;
import dev.shiza.uify.canvas.Canvas;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface Scene {

    static Scene newScene() {
        return new SceneImpl(
            ChestView.ofRows(3),
            Component.empty(),
            Collections.emptyList(),
            Collections.emptyList(),
            (holder, viewer) -> {},
            (holder, viewer) -> {});
    }

    Scene title(final Component title);

    Scene view(final SceneView view);

    Scene canvas(final Canvas canvas);

    Scene viewer(final Player viewer);

    Scene onSceneDispatch(final SceneGenericBehaviour<InventoryOpenEvent> sceneDispatchBehaviour);

    Scene onSceneClose(final SceneGenericBehaviour<InventoryCloseEvent> sceneCloseBehaviour);

    void dispatch();
}
