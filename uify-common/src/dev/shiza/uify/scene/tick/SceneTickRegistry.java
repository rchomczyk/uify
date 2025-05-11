package dev.shiza.uify.scene.tick;

import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.HashSet;
import java.util.Set;

public interface SceneTickRegistry {

    static SceneTickRegistry sceneTickRegistry() {
        return InstanceHolder.INSTANCE;
    }

    void register(final SceneInventoryHolder sceneInventoryHolder);

    void deregister(final SceneInventoryHolder sceneInventoryHolder);

    Set<SceneInventoryHolder> tickingSceneInventoryHolders();

    final class InstanceHolder {
        static final SceneTickRegistry INSTANCE = new SceneTickRegistryImpl(new HashSet<>());

        private InstanceHolder() {}
    }
}
