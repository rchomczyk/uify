package dev.shiza.uify.scene.tick;

import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.Set;

public final class SceneTickRegistryImpl implements SceneTickRegistry {

    private final Set<SceneInventoryHolder> tickingSceneInventoryHolders;

    public SceneTickRegistryImpl(final Set<SceneInventoryHolder> tickingSceneInventoryHolders) {
        this.tickingSceneInventoryHolders = tickingSceneInventoryHolders;
    }

    @Override
    public void register(final SceneInventoryHolder sceneInventoryHolder) {
        tickingSceneInventoryHolders.add(sceneInventoryHolder);
    }

    @Override
    public void deregister(final SceneInventoryHolder sceneInventoryHolder) {
        tickingSceneInventoryHolders.remove(sceneInventoryHolder);
    }

    @Override
    public Set<SceneInventoryHolder> tickingSceneInventoryHolders() {
        return Set.copyOf(tickingSceneInventoryHolders);
    }
}
