package dev.shiza.uify.canvas.element.cooldown.expiration;

import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class PaperExpirationSchedulerAccessor {

    private static final AtomicReference<ExpirationScheduler> PAPER_EXPIRATION_SCHEDULER = new AtomicReference<>();

    private PaperExpirationSchedulerAccessor() {}

    public static void initializePaperExpirationScheduler(final Plugin plugin) {
        PAPER_EXPIRATION_SCHEDULER.set(new PaperExpirationScheduler(plugin, plugin.getServer().getScheduler()));
    }

    public static ExpirationScheduler paperExpirationScheduler() {
        return PAPER_EXPIRATION_SCHEDULER.get();
    }
}
