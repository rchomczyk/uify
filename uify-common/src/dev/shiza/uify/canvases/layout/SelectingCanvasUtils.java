package dev.shiza.uify.canvases.layout;

import java.math.BigDecimal;
import java.math.BigInteger;

final class SelectingCanvasUtils {

    private SelectingCanvasUtils() {}

    @SuppressWarnings("unchecked")
    static <T extends Number> T convertToNumberType(final double value, final T original) {
        return switch (original) {
            case Integer ignored -> (T) Integer.valueOf((int) value);
            case Float ignored -> (T) Float.valueOf((float) value);
            case Long ignored -> (T) Long.valueOf((long) value);
            case Short ignored -> (T) Short.valueOf((short) value);
            case Byte ignored -> (T) Byte.valueOf((byte) value);
            case Double ignored -> (T) Double.valueOf(value);
            case BigDecimal ignored -> (T) BigDecimal.valueOf(value);
            case BigInteger ignored -> (T) BigInteger.valueOf((long) value);
            case null, default -> throw new IllegalArgumentException("Unsupported number type.");
        };
    }
}
