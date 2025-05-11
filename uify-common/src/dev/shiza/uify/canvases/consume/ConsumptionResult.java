package dev.shiza.uify.canvases.consume;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public record ConsumptionResult(List<ItemStack> items) implements ConsumptionResultMorph {
}
