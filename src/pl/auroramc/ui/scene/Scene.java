package pl.auroramc.ui.scene;

import java.util.Collections;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import pl.auroramc.ui.scene.view.ChestView;
import pl.auroramc.ui.scene.view.SceneView;
import pl.auroramc.ui.canvas.Canvas;

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
