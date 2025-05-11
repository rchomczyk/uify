package dev.shiza.uify.demo;

import dev.shiza.uify.Uify;
import dev.shiza.uify.time.MinecraftTimeEquivalent;
import java.time.Duration;
import java.util.Objects;
import org.bukkit.plugin.java.JavaPlugin;

public class DemoBukkitPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Configuration of uify is required for it to properly catch up all actions
        // happening within a scene.
        Uify.configure(this, Duration.ofMillis(MinecraftTimeEquivalent.MILLISECONDS_PER_TICK).multipliedBy(10));

        Objects.requireNonNull(getCommand("uify")).setExecutor(new DemoCommand());
    }

    @Override
    public void onDisable() {
        Uify.destroy();
    }
}
