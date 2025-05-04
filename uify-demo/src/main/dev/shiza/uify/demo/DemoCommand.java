package dev.shiza.uify.demo;

import dev.shiza.uify.canvas.consume.ConsumingCanvas;
import dev.shiza.uify.scene.SceneComposer;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import dev.shiza.uify.scene.view.ChestView;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

final class DemoCommand implements CommandExecutor, TabCompleter {

    private final Map<String, Consumer<Player>> scenarios;

    DemoCommand(final Plugin plugin) {
        this.scenarios = new HashMap<>();
        this.scenarios.put("kit-editor", kitEditor());
        this.scenarios.put("gradient-title", gradientTitle(plugin));
    }

    @Override
    public boolean onCommand(
        final @NotNull CommandSender source,
        final @NotNull Command command,
        final @NotNull String label,
        final @NotNull String @NotNull [] arguments) {
        if (!(source instanceof Player player)) {
            source.sendMessage("You cannot run scenarios from console.");
            return true;
        }

        if (arguments.length == 0) {
            final String availableScenarios = String.join(", ", scenarios.keySet());
            source.sendMessage("You need to choose available scenario: %s".formatted(availableScenarios));
            return true;
        }

        final String scenario = arguments[0];

        final Consumer<Player> resolvedScenario = scenarios.get(scenario);
        if (resolvedScenario == null) {
            source.sendMessage("Specified scenario does not exists.");
            return true;
        }

        resolvedScenario.accept(player);
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(
        final @NotNull CommandSender source,
        final @NotNull Command command,
        final @NotNull String label,
        final @NotNull String @NotNull [] arguments) {
        if (arguments.length == 1) {
            return scenarios.keySet().stream()
                .filter(scenario -> StringUtils.startsWithIgnoreCase(scenario, arguments[0]))
                .toList();
        }

        return Collections.emptyList();
    }

    private Consumer<Player> kitEditor() {
        final List<ItemStack> initialContent = List.of(
            new ItemStack(Material.DIAMOND),
            new ItemStack(Material.DIAMOND_SWORD));
        return viewer -> SceneComposer.compose(ChestView.ofRows(1))
            .title(MiniMessage.miniMessage().deserialize("<gray>Kit editor"))
            .viewer(viewer)
            .canvas(ConsumingCanvas.rows(1)
                .populateItems(initialContent)
                .onConsumption(((state, event, consumed) -> {
                    final String consumedTypes = consumed.stream()
                        .map(ItemStack::getType)
                        .map(Material::name)
                        .distinct()
                        .collect(Collectors.joining(","));
                    viewer.sendMessage("Consumed %d items, of types: %s".formatted(consumed.size(), consumedTypes));
                })))
            .dispatch();
    }

    private Consumer<Player> gradientTitle(final Plugin plugin) {
        return viewer -> {
            SceneComposer.compose(ChestView.ofRows(1))
                .title(MiniMessage.miniMessage()
                    .deserialize("<gradient:#5e4fa2:#f79459>Setting up rotations...</gradient>"))
                .viewer(viewer)
                .dispatch();

            final InventoryHolder activeHolder = viewer.getOpenInventory().getTopInventory().getHolder();
            if (!(activeHolder instanceof SceneInventoryHolder sceneInventoryHolder)) {
                return;
            }

            final BukkitScheduler scheduler = plugin.getServer().getScheduler();

            final AtomicReference<BukkitTask> taskReference = new AtomicReference<>();
            final AtomicInteger rotations = new AtomicInteger();

            final BukkitTask scheduledTask = scheduler.runTaskTimer(
                plugin, () -> {
                    final int current = rotations.incrementAndGet();
                    final BukkitTask resolvedTask = taskReference.get();
                    if (current > 5 && resolvedTask != null) {
                        resolvedTask.cancel();
                        return;
                    }

                    final Component newTitle = MiniMessage.miniMessage().deserialize(
                        "<gradient:#5e4fa2:#f79459>Current rotation: <rotation></gradient>",
                        Placeholder.unparsed("rotation", String.valueOf(current)));
                    sceneInventoryHolder.title(newTitle);
                }, 50L, 50L);
            taskReference.set(scheduledTask);
        };
    }
}
