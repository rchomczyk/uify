package dev.shiza.uify.time;

import java.time.Duration;

public class MinecraftTimeEquivalent {

    private static final long TICKS_PER_SECOND = 20;
    private static final long MILLISECONDS_PER_SECOND = 1000;
    private static final long MILLISECONDS_PER_TICK = MILLISECONDS_PER_SECOND / TICKS_PER_SECOND;

    public static final long TICK = 1;
    public static final long SECOND = TICK * 20;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;

    private MinecraftTimeEquivalent() {}

    public static long ticks(final Duration duration) {
        if (duration.isZero()) {
            return 0;
        }

        return TICK * (duration.toMillis() / MILLISECONDS_PER_TICK);
    }
}