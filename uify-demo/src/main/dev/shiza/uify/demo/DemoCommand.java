package dev.shiza.uify.demo;

import dev.shiza.uify.canvas.consume.ConsumingCanvas;
import dev.shiza.uify.canvas.element.inventory.ItemStackBuilder;
import dev.shiza.uify.canvas.layout.LayoutCanvas;
import dev.shiza.uify.canvas.layout.SelectingCanvas;
import dev.shiza.uify.canvas.paginated.PaginatedCanvas;
import dev.shiza.uify.canvas.sequential.SequentialCanvas;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.SceneComposer;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import dev.shiza.uify.scene.view.AnvilView;
import dev.shiza.uify.scene.view.ChestView;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        this.scenarios.put("border", border());
        this.scenarios.put("kit-editor", kitEditor());
        this.scenarios.put("anvil-flex", anvilFlex());
        this.scenarios.put("anvil-input", anvilInput());
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

    private Consumer<Player> border() {
        return viewer -> SceneComposer.compose(ChestView.ofRows(5))
            .title(Component.text("Border"))
            .viewer(viewer)
            .canvas(LayoutCanvas.border(3, 7, ItemStackBuilder.of(Material.BLACK_STAINED_GLASS_PANE).buildAsElement())
                .position(position -> position.minimum(1, 1).maximum(4, 7)))
            .dispatch();
    }

    private Consumer<Player> anvilFlex() {
        return viewer -> SceneComposer.compose(AnvilView.ofViewer(viewer))
            .title(Component.text("Anvil flex"))
            .canvas(PaginatedCanvas.rows(1)
                .compose(position -> position.minimum(0, 1).maximum(0, 1))
                .forward(
                    0, 0,
                    ItemStackBuilder.of(Material.ARROW).displayName(Component.text("Forward")).buildAsElement())
                .backward(
                    0, 2,
                    ItemStackBuilder.of(Material.ARROW).displayName(Component.text("Backward")).buildAsElement())
                .populate(Arrays.stream(Material.values())
                    .filter(Material::isItem)
                    .map(ItemStackBuilder::of)
                    .map(ItemStackBuilder::buildAsElement)
                    .toList()))
            .dispatch();
    }

    private Consumer<Player> anvilInput() {
        return viewer -> SceneComposer.compose(AnvilView.ofViewer(viewer))
            .title(Component.text("Anvil input"))
            .canvas(SequentialCanvas.rows(1)
                .elements(
                    ItemStackBuilder.of(Material.DIAMOND_SWORD)
                        .displayName(Component.text("Please specify name of your opponent"))
                        .buildAsElement()))
            .onSceneDispatch((state, event) -> state.holder()
                .anvilInventory()
                .onRenameConfirmation(renameState -> {
                    final boolean random = ThreadLocalRandom.current().nextBoolean();
                    if (random) {
                        viewer.sendMessage("Input: " + renameState.renameText());
                        renameState.inventory().renameText("good good");
                    } else {
                        renameState.inventory().renameText("try again bro");
                    }
                }))
            .dispatch();
    }

    private Consumer<Player> kitEditor() {
        return viewer -> SceneComposer.compose(ChestView.ofRows(5))
            .title(MiniMessage.miniMessage().deserialize("<gray>Kit editor"))
            .viewer(viewer)
            .canvas(ConsumingCanvas.rows(2)
                .position(position -> position.minimum(1, 1).maximum(2, 7))
                .populateItems(Map.of(
                    new Position(0, 1), new ItemStack(Material.DIAMOND),
                    new Position(1, 5), new ItemStack(Material.DIAMOND_SWORD)))
                .onIndexedConsumption((state, event, result) -> {
                    final String consumedTypes = result.items().entrySet().stream()
                        .map(itemByRawSlot ->
                            "row: " + itemByRawSlot.getKey().row() + ", " +
                                "col: " + itemByRawSlot.getKey().column() + ", " +
                                "type: " + itemByRawSlot.getValue().getType().name())
                        .collect(Collectors.joining("\n"));
                    final int consumedCount = result.items().size();
                    viewer.sendMessage("Consumed %d items:%n%s".formatted(consumedCount, consumedTypes));
                }))
            .canvas(SelectingCanvas.<Integer>pattern("+ = -")
                .position(position -> position.minimum(4, 2).maximum(4, 6))
                .display('=', amount -> ItemStackBuilder.of(Material.DIAMOND).amount(amount).buildAsElement())
                .mutator(
                    '+',
                    ItemStackBuilder.of(Material.PAPER)
                        .displayName(Component.text("+").color(NamedTextColor.GREEN))
                        .build(),
                    number -> number + 1)
                .mutator(
                    '-',
                    ItemStackBuilder.of(Material.PAPER)
                        .displayName(Component.text("-").color(NamedTextColor.RED))
                        .build(),
                    number -> number - 1)
                .minimum(1)
                .maximum(64)
                .amount(16))
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
