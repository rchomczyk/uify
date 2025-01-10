package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;

public interface Canvas extends CanvasBehaviours {

    Canvas position(final UnaryOperator<CanvasPosition> mutator);

    CanvasMapperRenderer mapper();

    @ApiStatus.Internal
    void assign(final SceneInventoryHolder owner);

    void update();

    <T extends Canvas> T typed(final Class<T> canvasType);

    <T extends Canvas> Optional<T> mapping(final Class<T> canvasType);

    <T extends Canvas, R> Optional<R> mapping(final Class<T> canvasType, final Function<T, R> mutator);
}
