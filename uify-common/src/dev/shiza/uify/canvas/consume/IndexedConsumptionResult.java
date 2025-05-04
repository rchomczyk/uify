package dev.shiza.uify.canvas.consume;

import dev.shiza.uify.position.Position;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public record IndexedConsumptionResult(
    Map<Position, ItemStack> items, Map<Integer, ItemStack> itemsByRawSlots) implements ConsumptionResultMorph {
}
