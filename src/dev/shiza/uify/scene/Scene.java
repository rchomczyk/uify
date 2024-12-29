package dev.shiza.uify.scene;

import java.util.Collections;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import dev.shiza.uify.scene.view.ChestView;
import dev.shiza.uify.scene.view.SceneView;
import dev.shiza.uify.canvas.Canvas;

public interface Scene {

    static Scene newScene() {
        return new SceneImpl(ChestView.ofRows(3), Component.empty(), Collections.emptyList(), Collections.emptyList());
    }

    Scene title(final Component title);

    Scene view(final SceneView view);

    Scene canvas(final Canvas canvas);

    Scene viewer(final Player viewer);

    void dispatch();
}
