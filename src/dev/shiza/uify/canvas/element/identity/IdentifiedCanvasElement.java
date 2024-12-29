package dev.shiza.uify.canvas.element.identity;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasElement;

public record IdentifiedCanvasElement(Canvas canvas, CanvasElement element) {}