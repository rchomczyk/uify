package dev.shiza.uify.scene;

import dev.shiza.uify.scene.behaviour.SceneGenericBehaviour;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface SceneBehaviours {

    Scene onSceneDispatch(final SceneGenericBehaviour<InventoryOpenEvent> sceneDispatchBehaviour);

    Scene onSceneClose(final SceneGenericBehaviour<InventoryCloseEvent> sceneCloseBehaviour);
}
