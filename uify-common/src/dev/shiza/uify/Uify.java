package dev.shiza.uify;

import dev.shiza.uify.canvas.element.CanvasElementInteractionListener;
import dev.shiza.uify.scene.SceneInteractionListener;
import dev.shiza.uify.scene.tick.SceneTickTask;
import dev.shiza.uify.time.MinecraftTimeEquivalent;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

public final class Uify {

    private static final AtomicBoolean HAS_SCENE_TICKING = new AtomicBoolean();

    private Uify() {}

    public static void configure(final Plugin plugin) {
        configure(plugin, Duration.ZERO);
    }

    public static void configure(final Plugin plugin, final Duration tickInterval) {
        plugin.getServer().getPluginManager().registerEvents(new SceneInteractionListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CanvasElementInteractionListener(), plugin);
        if (tickInterval.compareTo(Duration.ZERO) > 0) {
            final long intervalInTicks = MinecraftTimeEquivalent.ticks(tickInterval);
            plugin.getServer()
                .getScheduler()
                .runTaskTimer(plugin, new SceneTickTask(), intervalInTicks, intervalInTicks);
            HAS_SCENE_TICKING.set(true);
        }
    }

    @ApiStatus.Internal
    public static boolean hasSceneTicking() {
        return HAS_SCENE_TICKING.get();
    }
}
