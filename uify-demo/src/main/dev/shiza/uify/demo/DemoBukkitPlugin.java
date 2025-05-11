package dev.shiza.uify.demo;

import dev.shiza.uify.Uify;
import java.time.Duration;
import java.util.Objects;
import org.bukkit.plugin.java.JavaPlugin;

public class DemoBukkitPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Configuration of uify is required for it to properly catch up all actions
        // happening within a scene.
        Uify.configure(this, Duration.ofSeconds(2));

        Objects.requireNonNull(getCommand("uify")).setExecutor(new DemoCommand());
    }
}
