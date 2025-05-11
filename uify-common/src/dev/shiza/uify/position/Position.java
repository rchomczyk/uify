package dev.shiza.uify.position;

import org.jetbrains.annotations.Range;

public record Position(@Range(from = 0, to = 5) int row, @Range(from = 0, to = 8) int column) {
}
