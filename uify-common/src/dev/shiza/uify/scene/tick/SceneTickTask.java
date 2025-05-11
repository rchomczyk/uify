package dev.shiza.uify.scene.tick;

import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.HumanEntity;

public final class SceneTickTask implements Runnable {

    @Override
    public void run() {
        final SceneTickRegistry sceneTickRegistry = SceneTickRegistry.sceneTickRegistry();

        final Set<SceneInventoryHolder> sceneInventoryHolders = sceneTickRegistry.tickingSceneInventoryHolders();
        if (sceneInventoryHolders.isEmpty()) {
            return;
        }

        for (final SceneInventoryHolder sceneInventoryHolder : sceneInventoryHolders) {
            final List<HumanEntity> viewers = sceneInventoryHolder.getInventory().getViewers();
            if (viewers.isEmpty()) {
                sceneTickRegistry.deregister(sceneInventoryHolder);
                continue;
            }

            final SceneTickBehaviour sceneTickBehaviour =
                ((SceneImpl) sceneInventoryHolder.sceneMorph()).sceneTickBehaviour();
            sceneTickBehaviour.accept(sceneInventoryHolder);
        }
    }
}
