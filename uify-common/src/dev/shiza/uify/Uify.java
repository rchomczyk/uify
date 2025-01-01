package dev.shiza.uify;

import dev.shiza.uify.canvas.element.CanvasElementInteractionListener;
import dev.shiza.uify.scene.SceneInteractionListener;
import org.bukkit.plugin.Plugin;

public final class Uify {

    private Uify() {}

    public static void configure(final Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new SceneInteractionListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CanvasElementInteractionListener(), plugin);
    }
}
