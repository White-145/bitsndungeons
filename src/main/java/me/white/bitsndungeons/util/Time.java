package me.white.bitsndungeons.util;

public class Time {
    public static final long TIME_STARTED = System.nanoTime();
    private static final long NANO_PER_SEC = 1000000000;

    public static double getTime() {
        return (double)(System.nanoTime() - TIME_STARTED) / NANO_PER_SEC;
    }
}
