package dev.shiza.uify.scene;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import dev.shiza.uify.scene.view.SceneView;
import dev.shiza.uify.canvas.Canvas;

public interface Scene extends SceneBehaviours {

    Scene title(final Component title);

    Scene view(final SceneView view);

    Scene canvas(final Canvas canvas);

    Scene viewer(final Player viewer);

    void dispatch();
}
