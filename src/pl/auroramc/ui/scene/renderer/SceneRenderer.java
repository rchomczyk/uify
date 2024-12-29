package pl.auroramc.ui.scene.renderer;

import pl.auroramc.ui.scene.Scene;
import pl.auroramc.ui.scene.inventory.SceneInventoryHolder;

public interface SceneRenderer {

    static SceneRenderer sceneRenderer() {
        return InstanceHolder.INSTANCE;
    }

    SceneInventoryHolder renderScene(final Scene sceneMorph);

    SceneInventoryHolder renderScene(final Scene sceneMorph, final SceneInventoryHolder sceneInventoryHolder);

    class InstanceHolder {
        private static final SceneRenderer INSTANCE = new SceneRendererImpl();

        private InstanceHolder() {
        }
    }
}
