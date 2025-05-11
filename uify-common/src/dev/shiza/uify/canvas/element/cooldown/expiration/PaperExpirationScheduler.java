package dev.shiza.uify.canvas.element.cooldown.expiration;

import dev.shiza.uify.time.MinecraftTimeEquivalent;
import java.time.Duration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

final class PaperExpirationScheduler implements ExpirationScheduler {

    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public PaperExpirationScheduler(final Plugin plugin, final BukkitScheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    @Override
    public Runnable scheduleAtFixedRate(final Runnable task, final int initialDelay, final int period) {
        final BukkitTask future = scheduler.runTaskTimer(
            plugin,
            task,
            MinecraftTimeEquivalent.ticks(Duration.ofSeconds(initialDelay)),
            MinecraftTimeEquivalent.ticks(Duration.ofSeconds(period)));
        return future::cancel;
    }

    @Override
    public void shutdown() {
    }
}

