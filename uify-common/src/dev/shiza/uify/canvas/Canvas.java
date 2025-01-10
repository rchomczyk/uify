package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Canvas extends CanvasBehaviours {

    Canvas position(final UnaryOperator<CanvasPosition> mutator);

    CanvasMapperRenderer mapper();

    <T extends Canvas> T typed(final Class<T> canvasType);

    <T extends Canvas> Optional<T> mapping(final Class<T> canvasType);

    <T extends Canvas, R> Optional<R> mapping(final Class<T> canvasType, final Function<T, R> mutator);
}
