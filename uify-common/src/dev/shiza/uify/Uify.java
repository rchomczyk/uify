package dev.shiza.uify;

import dev.shiza.uify.canvas.element.CanvasElementInteractionListener;
import dev.shiza.uify.scene.SceneInteractionListener;
import dev.shiza.uify.scene.tick.SceneTickTask;
import dev.shiza.uify.time.MinecraftTimeEquivalent;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

public final class Uify {

    private static final AtomicBoolean HAS_TICKING_SUPPORT = new AtomicBoolean();

    private Uify() {}

    public static void configure(final Plugin plugin) {
        configure(plugin, Duration.ZERO);
    }

    @ApiStatus.Experimental
    public static void configure(final Plugin plugin, final Duration tickInterval) {
        plugin.getServer().getPluginManager().registerEvents(new SceneInteractionListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CanvasElementInteractionListener(), plugin);
        if (tickInterval.compareTo(Duration.ZERO) > 0) {
            plugin.getComponentLogger().info(tickingNotice());

            final long intervalInTicks = MinecraftTimeEquivalent.ticks(tickInterval);
            plugin.getServer()
                .getScheduler()
                .runTaskTimer(plugin, new SceneTickTask(), intervalInTicks, intervalInTicks);

            HAS_TICKING_SUPPORT.set(true);
        }
    }

    @ApiStatus.Internal
    public static boolean hasTickingSupport() {
        return HAS_TICKING_SUPPORT.get();
    }

    @ApiStatus.Internal
    private static Component tickingNotice() {
        return Component.text(
                "You are using experimental support for uify ticking, it significantly could affect performance of your server.")
            .color(NamedTextColor.RED);
    }
}
