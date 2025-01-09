package dev.shiza.uify.scene;

import dev.shiza.uify.scene.view.ChestView;
import dev.shiza.uify.scene.view.SceneView;
import java.util.Collections;
import net.kyori.adventure.text.Component;

public final class SceneComposer {

    private SceneComposer() {}

    public static Scene compose() {
        return new SceneImpl(
            ChestView.ofRows(3),
            Component.empty(),
            Collections.emptyList(),
            Collections.emptyList(),
            (holder, viewer) -> {},
            (holder, viewer) -> {});
    }

    public static Scene compose(final SceneView sceneView) {
        return compose().view(sceneView);
    }
}
