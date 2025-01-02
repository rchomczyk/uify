package dev.shiza.uify.canvas.consume;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import org.bukkit.inventory.ItemStack;

public interface ConsumingCanvas extends Canvas {

    static ConsumingCanvas ofRows(final int rows) {
        return new ConsumingCanvasImpl(new ArrayList<>())
            .position(
                position -> position
                    .minimum(0, 0)
                    .maximum(rows - 1, 8));
    }

    ConsumingCanvas populate(final List<ItemStack> items);

    @Override
    ConsumingCanvas position(final UnaryOperator<CanvasPosition> mutator);

    List<ItemStack> consume(final SceneInventoryHolder holder);
}
