package dev.shiza.uify.scene.renderer;

import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

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
