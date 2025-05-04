package dev.shiza.uify.canvas.consume;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public record ConsumptionResult(List<ItemStack> items) implements ConsumptionResultMorph {
}
