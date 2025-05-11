package dev.shiza.uify.demo;

import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.element.ImmutableCanvasElement;
import dev.shiza.uify.canvases.consume.ConsumingCanvas;
import dev.shiza.uify.canvases.layout.LayoutCanvas;
import dev.shiza.uify.canvases.layout.SelectingCanvas;
import dev.shiza.uify.canvases.paginated.PaginatedCanvas;
import dev.shiza.uify.canvases.sequential.SequentialCanvas;
import dev.shiza.uify.inventory.item.ItemStackBuilder;
import dev.shiza.uify.inventory.item.ItemStacks;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.SceneComposer;
import dev.shiza.uify.scene.view.AnvilView;
import dev.shiza.uify.scene.view.ChestView;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class DemoCommand implements CommandExecutor, TabCompleter {

    private final Map<String, Consumer<Player>> scenarios;

    DemoCommand() {
        this.scenarios = new HashMap<>();
        this.scenarios.put("border", border());
        this.scenarios.put("editor", kitEditor());
        this.scenarios.put("input", input());
        this.scenarios.put("animated", animated());
        this.scenarios.put("leather", leatherArmor());
        this.scenarios.put("cooldown", cooldowns());
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

    private Consumer<Player> cooldowns() {
        return viewer -> SceneComposer.compose(ChestView.ofRows(1))
            .title(Component.text("Cooldowns"))
            .viewer(viewer)
            .canvas(SequentialCanvas.rows(1)
                .elements(ItemStackBuilder.of(Material.DIAMOND_SWORD)
                    .displayName(Component.text("Click me"))
                    .buildAsElement()
                    .onElementClick((state, event) ->
                        event.getWhoClicked()
                            .sendMessage(Component.text("Nice! Click has been processed")))
                    .onElementCooldown((state, event) ->
                        event.getWhoClicked()
                            .sendMessage(MiniMessage.miniMessage().deserialize(
                                "You need to wait for next <time>",
                                Placeholder.unparsed("time", state.remainingPeriod().toString()))))
                    .onElementCooldownExpiration((state, event) ->
                        event.getWhoClicked().sendMessage(Component.text("You can click me again!")))
                    .cooldown(Duration.ofSeconds(5))))
            .dispatch();
    }

    private Consumer<Player> border() {
        return viewer -> SceneComposer.compose(ChestView.ofRows(6))
            .title(Component.text("Border"))
            .viewer(viewer)
            .canvas(LayoutCanvas.border(
                    4,
                    7,
                    new ImmutableCanvasElement(() -> ItemStacks.namelessItem(Material.BLACK_STAINED_GLASS_PANE)))
                .position(position -> position.minimum(1, 1).maximum(4, 7)))
            .canvas(SequentialCanvas.rows(3)
                .position(position -> position.minimum(2, 2).maximum(3, 6))
                .elements(Arrays.stream(Material.values())
                    .filter(Predicate.not(Material::isAir))
                    .filter(Material::isItem)
                    .map(ItemStackBuilder::of)
                    .map(ItemStackBuilder::buildAsElement)
                    .limit(10)
                    .toList()))
            .dispatch();
    }

    private Consumer<Player> input() {
        return viewer -> SceneComposer.compose(AnvilView.ofViewer(viewer))
            .title(Component.text("Anvil input"))
            .canvas(LayoutCanvas.pattern("x y")
                .bind(
                    'x',
                    ItemStackBuilder.of(Material.DIAMOND_SWORD)
                        .displayName(Component.text("Please specify name of your opponent"))
                        .buildAsElement())
                .bind(
                    'y',
                    ItemStackBuilder.of(Material.DIAMOND_SWORD)
                        .buildAsElement()
                        .onElementClick((state, event) -> {
                            event.getWhoClicked()
                                .sendMessage(state.holder().anvilInventory().renameText());
                            state.holder().anvilInventory().renameText("Input has been procesed");
                        })))
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

    private Consumer<Player> animated() {
        return viewer -> {
            final LongAdder currentTick = new LongAdder();
            SceneComposer.compose(ChestView.ofRows(4))
                .title(MiniMessage.miniMessage().deserialize("<gradient:#5e4fa2:#f79459>Current tick: N/A</gradient>"))
                .viewer(viewer)
                .canvas(
                    PaginatedCanvas.rows(4)
                        .compose(position -> position.minimum(0, 0).maximum(0, 8))
                        .compose(position -> position.minimum(1, 0).maximum(2, 0))
                        .compose(position -> position.minimum(1, 8).maximum(2, 8))
                        .compose(position -> position.minimum(3, 0).maximum(3, 8))
                        .populate(shuffledElements(0))
                        .onPaginatedCanvasTick(state -> {
                            state.canvas()
                                .populate(shuffledElements(currentTick.intValue()), true)
                                .update();
                            return Duration.ofMillis(300);
                        }))
                .canvas(SequentialCanvas.rows(2)
                    .position(canvasPosition -> canvasPosition.minimum(1, 1).maximum(2, 7))
                    .elements(randomElements())
                    .onSequentialCanvasTick(state -> {
                        state.canvas().elements(randomElements(), true);
                        state.canvas().update();
                        return Duration.ofSeconds(5);
                    }))
                .onSceneTick(holder -> {
                    currentTick.increment();
                    holder.title(MiniMessage.miniMessage().deserialize(
                        "<gradient:#5e4fa2:#f79459>Current tick: <tick></gradient>",
                        Placeholder.unparsed("tick", String.valueOf(currentTick.longValue()))));
                })
                .dispatch();
        };
    }

    private Consumer<Player> leatherArmor() {
        return viewer -> SceneComposer.compose(ChestView.ofRows(1))
            .title(Component.text("Leather armor"))
            .viewer(viewer)
            .canvas(SequentialCanvas.rows(1)
                .elements(
                    new ImmutableCanvasElement(() -> ItemStacks.leatherArmor(Material.LEATHER_HELMET)),
                    new ImmutableCanvasElement(() -> ItemStacks.leatherArmor(Material.LEATHER_CHESTPLATE)),
                    new ImmutableCanvasElement(() -> ItemStacks.leatherArmor(Material.LEATHER_LEGGINGS)),
                    new ImmutableCanvasElement(() -> ItemStacks.leatherArmor(Material.LEATHER_BOOTS)))
                .onSequentialCanvasTick(state -> {
                    state.canvas().update();
                    return Duration.ofSeconds(10);
                }))
            .dispatch();
    }

    private List<CanvasElement> shuffledElements(final int tick) {
        return IntStream.rangeClosed(0, 21)
            .mapToObj(index -> (tick % 2 == index % 2) ?
                Material.WHITE_STAINED_GLASS_PANE :
                Material.BLUE_STAINED_GLASS_PANE)
            .map(ItemStacks::namelessItem)
            .map(itemStack -> new ImmutableCanvasElement(() -> itemStack))
            .map(CanvasElement.class::cast)
            .toList();
    }

    private List<CanvasElement> randomElements() {
        final List<CanvasElement> allElements = Arrays.stream(Material.values())
            .filter(Predicate.not(Material::isAir))
            .filter(Material::isItem)
            .map(ItemStackBuilder::of)
            .map(ItemStackBuilder::buildAsElement)
            .collect(Collectors.toList());

        Collections.shuffle(allElements);
        return allElements.stream()
            .limit(ThreadLocalRandom.current().nextInt(1, 15))
            .toList();
    }
}
